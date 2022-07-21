package id.holigo.services.holigoemailservice.web.mappers;

import id.holigo.services.holigoemailservice.domain.EmailVerification;
import id.holigo.services.holigoemailservice.web.model.EmailVerificationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class EmailVerificationMapperDecorator implements EmailVerificationMapper {

    private MessageSource messageSource;

    private EmailVerificationMapper emailVerificationMapper;

    @Value("${email.verification.image.url}")
    private String imageUrl;

    @Autowired
    public void setEmailVerificationMapper(EmailVerificationMapper emailVerificationMapper) {
        this.emailVerificationMapper = emailVerificationMapper;
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public EmailVerificationDto emailVerificationToEmailVerificationDto(EmailVerification emailVerification) {
        Object[] args = new Object[]{emailVerification.getEmail()};
        String messages = messageSource.getMessage(emailVerification.getIndexNote(), args, LocaleContextHolder.getLocale());
        EmailVerificationDto emailVerificationDto = emailVerificationMapper.emailVerificationToEmailVerificationDto(emailVerification);
        emailVerificationDto.setImageUrl(imageUrl);
        emailVerificationDto.setNotes(new ArrayList<>(Arrays.asList(messages.split("\\|"))));
        return emailVerificationDto;
    }
}
