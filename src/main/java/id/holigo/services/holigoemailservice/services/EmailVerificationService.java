package id.holigo.services.holigoemailservice.services;

import id.holigo.services.holigoemailservice.domain.EmailVerification;
import id.holigo.services.holigoemailservice.web.model.EmailVerificationDto;

public interface EmailVerificationService {
    EmailVerification createEmail(EmailVerificationDto emailVerificationDto, Long userId);
}
