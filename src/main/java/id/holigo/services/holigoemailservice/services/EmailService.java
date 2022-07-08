package id.holigo.services.holigoemailservice.services;

import id.holigo.services.common.EmailDto;

public interface EmailService {
    void sendEmail(EmailDto emailDto);
}
