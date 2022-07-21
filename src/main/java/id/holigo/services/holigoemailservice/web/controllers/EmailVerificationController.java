package id.holigo.services.holigoemailservice.web.controllers;

import feign.RetryableException;
import id.holigo.services.common.model.EmailDto;
import id.holigo.services.common.model.EmailStatusEnum;
import id.holigo.services.common.model.EmailTypeEnum;
import id.holigo.services.common.model.UserDto;
import id.holigo.services.holigoemailservice.config.KafkaTopicConfig;
import id.holigo.services.holigoemailservice.domain.EmailVerification;
import id.holigo.services.holigoemailservice.repositories.EmailVerificationRepository;
import id.holigo.services.holigoemailservice.services.EmailStatusService;
import id.holigo.services.holigoemailservice.services.user.UserService;
import id.holigo.services.holigoemailservice.web.mappers.EmailVerificationMapper;
import id.holigo.services.holigoemailservice.web.model.EmailVerificationDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
public class EmailVerificationController {

    private static final String PATH = "/api/v1/emailVerifications";

    private static final String EMAIL_VERIFICATION_ADDRESS = "noreply@holigo.co.id";

    private MessageSource messageSource;

    @Value("${service.domain.url}")
    private String domain;

    private EmailVerificationRepository emailVerificationRepository;

    private EmailVerificationMapper emailVerificationMapper;

    private UserService userService;

    private EmailStatusService emailStatusService;
    private KafkaTemplate<String, EmailDto> kafkaTemplate;

    @Autowired
    public void setEmailVerificationRepository(EmailVerificationRepository emailVerificationRepository) {
        this.emailVerificationRepository = emailVerificationRepository;
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Autowired
    public void setKafkaTemplate(KafkaTemplate<String, EmailDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Autowired
    public void setEmailVerificationMapper(EmailVerificationMapper emailVerificationMapper) {
        this.emailVerificationMapper = emailVerificationMapper;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setEmailStatusService(EmailStatusService emailStatusService) {
        this.emailStatusService = emailStatusService;
    }

    @PostMapping(PATH)
    public ResponseEntity<HttpStatus> create(@RequestBody EmailVerificationDto emailVerificationDto,
                                             @RequestHeader("user-id") Long userId) {
        UserDto user;
        try {
            user = userService.getUser(userId);
        } catch (RetryableException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (!Objects.equals(user.getEmail(), emailVerificationDto.getEmail())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        EmailVerification emailVerification;
        Optional<EmailVerification> fetchEmailVerification = emailVerificationRepository.findByUserIdAndStatus(userId, EmailStatusEnum.WAITING_CONFIRMATION);
        if (fetchEmailVerification.isPresent()) {
            emailVerification = fetchEmailVerification.get();
        } else {
            emailVerification = emailVerificationMapper.emailVerificationDtoToEmailVerification(emailVerificationDto);
            emailVerification.setUserId(userId);
            long addMinutes = 5L;
            emailVerification.setExpiredAt(Timestamp.valueOf(LocalDateTime.now().plusMinutes(addMinutes)));
            emailVerification.setType(EmailTypeEnum.VERIFICATION_EMAIL);
            emailVerification.setStatus(EmailStatusEnum.WAITING_CONFIRMATION);
            emailVerification.setVerificationCode(user.getVerificationCode());
            emailVerification.setIndexNote("email_verification");
            emailVerification = emailVerificationRepository.save(emailVerification);
        }
        Object[] args = new Object[]{user.getName(), domain, user.getVerificationCode()};
        String content = messageSource.getMessage("email.content.verification", args, LocaleContextHolder.getLocale());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(UriComponentsBuilder.fromPath(PATH + "/{id}").buildAndExpand(emailVerification.getId()).toUri());
        EmailDto emailDto = EmailDto.builder()
                .to(emailVerification.getEmail())
                .from(EMAIL_VERIFICATION_ADDRESS)
                .subject(messageSource.getMessage("email.subject.verification", null, LocaleContextHolder.getLocale()))
                .content(StringEscapeUtils.unescapeJava(content))
                .build();
        kafkaTemplate.send(KafkaTopicConfig.EMAIL_VERIFICATION, emailDto);
        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @GetMapping(PATH + "/{id}")
    public ResponseEntity<EmailVerificationDto> show(@PathVariable("id") UUID id, @RequestHeader("user-id") Long userId) {

        Optional<EmailVerification> fetchEmailVerification = emailVerificationRepository.findById(id);
        if (fetchEmailVerification.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        EmailVerification emailVerification = fetchEmailVerification.get();
        if (!emailVerification.getUserId().equals(userId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(emailVerificationMapper
                .emailVerificationToEmailVerificationDto(emailVerification), HttpStatus.OK);
    }

    @GetMapping("emailVerifications")
    public void verified(@RequestParam("verificationCode") String verificationCode) {
        Optional<EmailVerification> fetchEmailVerification = emailVerificationRepository.findByVerificationCodeAndStatus(
                verificationCode, EmailStatusEnum.WAITING_CONFIRMATION
        );
        fetchEmailVerification.ifPresent(emailVerification -> emailStatusService.successConfirmation(emailVerification.getId()));
    }
}
