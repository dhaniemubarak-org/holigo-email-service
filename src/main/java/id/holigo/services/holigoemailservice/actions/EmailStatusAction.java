package id.holigo.services.holigoemailservice.actions;

import id.holigo.services.common.model.EmailStatusEnum;
import id.holigo.services.common.model.UpdateUserEmailStatusDto;
import id.holigo.services.holigoemailservice.config.KafkaTopicConfig;
import id.holigo.services.holigoemailservice.domain.EmailVerification;
import id.holigo.services.holigoemailservice.events.EmailStatusEvent;
import id.holigo.services.holigoemailservice.repositories.EmailVerificationRepository;
import id.holigo.services.holigoemailservice.services.EmailStatusServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class EmailStatusAction {

    private final KafkaTemplate<String, UpdateUserEmailStatusDto> updateUserKafkaTemplate;

    private final EmailVerificationRepository emailVerificationRepository;

    public Action<EmailStatusEnum, EmailStatusEvent> processVerified() {

        return stateContext -> {
            log.info("Process verified is running....");
            log.info("context -> {}", stateContext.getMessageHeader(EmailStatusServiceImpl.EMAIL_VERIFICATION_HEADER).toString());
            log.info("last state -> {}", stateContext.getTarget().getId());
            EmailVerification emailVerification = emailVerificationRepository
                    .getReferenceById(UUID.fromString(stateContext.getMessageHeader(EmailStatusServiceImpl.EMAIL_VERIFICATION_HEADER).toString()));
            UpdateUserEmailStatusDto updateUserEmailStatusDto = UpdateUserEmailStatusDto.builder()
                    .userId(emailVerification.getUserId())
                    .emailStatus(stateContext.getTarget().getId()).build();
            log.info("UserUpdateEmailStatus -> {}", updateUserEmailStatusDto.getEmailStatus());
            updateUserKafkaTemplate.send(KafkaTopicConfig.USER_UPDATE_EMAIL_STATUS, updateUserEmailStatusDto);
        };
    }
}
