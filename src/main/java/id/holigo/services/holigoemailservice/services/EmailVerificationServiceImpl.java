package id.holigo.services.holigoemailservice.services;

import feign.RetryableException;
import id.holigo.services.common.model.EmailDto;
import id.holigo.services.common.model.EmailStatusEnum;
import id.holigo.services.common.model.EmailTypeEnum;
import id.holigo.services.common.model.UserDto;
import id.holigo.services.holigoemailservice.config.KafkaTopicConfig;
import id.holigo.services.holigoemailservice.domain.EmailVerification;
import id.holigo.services.holigoemailservice.repositories.EmailVerificationRepository;
import id.holigo.services.holigoemailservice.services.firebase.DynamicLinkService;
import id.holigo.services.holigoemailservice.services.user.UserService;
import id.holigo.services.holigoemailservice.web.exceptions.EmailNotMatchException;
import id.holigo.services.holigoemailservice.web.exceptions.UserNotFoundException;
import id.holigo.services.holigoemailservice.web.mappers.EmailVerificationMapper;
import id.holigo.services.holigoemailservice.web.model.DynamicLinkDto;
import id.holigo.services.holigoemailservice.web.model.EmailVerificationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {

    @Value("${email.verification.timeLimit}")
    private String emailVerificationTimeLimit;

    @Value("${email.verification.redirectUrl}")
    private String emailVerificationRedirectUrl;

    @Value("${email.verification.shortDomain}")
    private String emailVerificationShortDomain;

    @Value("${email.verification.address}")
    private String emailVerificationAddress;

    private UserService userService;

    private EmailVerificationRepository emailVerificationRepository;

    private EmailVerificationMapper emailVerificationMapper;

    private MessageSource messageSource;
    private KafkaTemplate<String, EmailDto> kafkaTemplate;

    private DynamicLinkService dynamicLinkService;

    @Autowired
    public void setDynamicLinkService(DynamicLinkService dynamicLinkService) {
        this.dynamicLinkService = dynamicLinkService;
    }

    @Autowired
    public void setKafkaTemplate(KafkaTemplate<String, EmailDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Autowired
    public void setEmailVerificationMapper(EmailVerificationMapper emailVerificationMapper) {
        this.emailVerificationMapper = emailVerificationMapper;
    }

    @Autowired
    public void setEmailVerificationRepository(EmailVerificationRepository emailVerificationRepository) {
        this.emailVerificationRepository = emailVerificationRepository;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public EmailVerification createEmail(EmailVerificationDto emailVerificationDto, Long userId) {
        UserDto user;
        try {
            user = userService.getUser(userId);
        } catch (RetryableException e) {
            throw new UserNotFoundException();
        }
        if (!Objects.equals(user.getEmail(), emailVerificationDto.getEmail())) {
            throw new EmailNotMatchException();
        }

        EmailVerification emailVerification;
        Optional<EmailVerification> fetchEmailVerification = emailVerificationRepository.findByUserIdAndStatus(userId, EmailStatusEnum.WAITING_CONFIRMATION);
        if (fetchEmailVerification.isPresent()) {
            emailVerification = fetchEmailVerification.get();
        } else {
            emailVerification = emailVerificationMapper.emailVerificationDtoToEmailVerification(emailVerificationDto);
            emailVerification.setUserId(userId);
            emailVerification.setExpiredAt(Timestamp.valueOf(LocalDateTime.now().plusMinutes(Long.parseLong(emailVerificationTimeLimit))));
            emailVerification.setType(EmailTypeEnum.VERIFICATION_EMAIL);
            emailVerification.setStatus(EmailStatusEnum.WAITING_CONFIRMATION);
            emailVerification.setVerificationCode(user.getVerificationCode());
            emailVerification.setIndexNote("email_verification");
            emailVerification = emailVerificationRepository.save(emailVerification);
        }
        String longUrl = emailVerificationShortDomain + "/?link=" + emailVerificationRedirectUrl + "?verificationCode=" + user.getVerificationCode();
        DynamicLinkDto dynamicLinkDto = dynamicLinkService.getShortLink(longUrl);
        String verificationUrl = dynamicLinkDto.getShortLink();
        String messages = messageSource.getMessage("email.content.verification", null, LocaleContextHolder.getLocale());
        List<String> notes = new ArrayList<>(Arrays.asList(messages.split("\\|")));
        Map<String, Object> data = new HashMap<>();
        data.put("userName", user.getName());
        data.put("verificationUrl", verificationUrl);
        data.put("notes", notes);
        EmailDto emailDto = EmailDto.builder()
                .to(emailVerification.getEmail())
                .from(emailVerificationAddress)
                .subject(messageSource.getMessage("email.subject.verification", null, LocaleContextHolder.getLocale()))
                .template("email-verification")
                .data(data)
                .build();
        kafkaTemplate.send(KafkaTopicConfig.EMAIL_VERIFICATION, emailDto);
        return emailVerification;
    }
}
