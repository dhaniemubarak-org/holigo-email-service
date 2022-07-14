package id.holigo.services.holigoemailservice.services;

import id.holigo.services.common.model.EmailDto;

public interface EmailService {
    void sendEmail(EmailDto emailDto);
}
