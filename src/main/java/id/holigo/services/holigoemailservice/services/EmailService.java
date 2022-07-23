package id.holigo.services.holigoemailservice.services;

import id.holigo.services.common.model.EmailDto;

import javax.mail.MessagingException;

public interface EmailService {
    void sendEmail(EmailDto emailDto) throws MessagingException;

    void sendSimpleMail(EmailDto emailDto);
}
