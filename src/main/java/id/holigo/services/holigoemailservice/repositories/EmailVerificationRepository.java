package id.holigo.services.holigoemailservice.repositories;

import id.holigo.services.common.model.EmailStatusEnum;
import id.holigo.services.holigoemailservice.domain.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, UUID> {

    Optional<EmailVerification> findByVerificationCodeAndStatus(String verificationCode, EmailStatusEnum status);

    Optional<EmailVerification> findByUserIdAndStatus(Long userId, EmailStatusEnum status);

    List<EmailVerification> findAllByStatusAndExpiredAtLessThan(EmailStatusEnum status, Timestamp now);
}
