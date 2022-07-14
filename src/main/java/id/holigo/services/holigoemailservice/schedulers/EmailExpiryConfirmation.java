package id.holigo.services.holigoemailservice.schedulers;

import id.holigo.services.common.model.EmailStatusEnum;
import id.holigo.services.holigoemailservice.domain.EmailVerification;
import id.holigo.services.holigoemailservice.repositories.EmailVerificationRepository;
import id.holigo.services.holigoemailservice.services.EmailStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmailExpiryConfirmation {

    private final EmailVerificationRepository emailVerificationRepository;

    private final EmailStatusService emailStatusService;

    @Scheduled(fixedRate = 10000)
    @Transactional
    public void toExpiryConfirmation() {
        List<EmailVerification> emailVerifications = emailVerificationRepository
                .findAllByStatusAndExpiredAtLessThan(
                        EmailStatusEnum.WAITING_CONFIRMATION,
                        Timestamp.valueOf(LocalDateTime.now()));
        if (emailVerifications.size() > 0) {
            emailVerifications.forEach(emailVerification -> emailStatusService.expiryConfirmation(emailVerification.getId()));
        }
    }
}
