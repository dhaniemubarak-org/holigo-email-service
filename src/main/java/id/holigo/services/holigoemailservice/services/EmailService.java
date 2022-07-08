package id.holigo.services.holigoemailservice.services;

import id.holigo.services.holigoemailservice.web.model.EmailDto;

public interface EmailService {
    void sendEmail(EmailDto emailDto);
}
