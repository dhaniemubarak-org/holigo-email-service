package id.holigo.services.holigoemailservice.services;

import id.holigo.services.common.model.EmailStatusEnum;
import id.holigo.services.holigoemailservice.domain.EmailVerification;
import id.holigo.services.holigoemailservice.events.EmailStatusEvent;
import id.holigo.services.holigoemailservice.interceptors.EmailStatusInterceptor;
import id.holigo.services.holigoemailservice.repositories.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailStatusServiceImpl implements EmailStatusService {


    private final StateMachineFactory<EmailStatusEnum, EmailStatusEvent> emailStatusStateMachineFactory;

    private EmailVerificationRepository emailVerificationRepository;

    private final EmailStatusInterceptor emailStatusInterceptor;

    @Autowired
    public void setEmailVerificationRepository(EmailVerificationRepository emailVerificationRepository) {
        this.emailVerificationRepository = emailVerificationRepository;
    }

    public static final String EMAIL_VERIFICATION_HEADER = "email_verification_id";

    @Override
    public void successConfirmation(UUID id) {
        StateMachine<EmailStatusEnum, EmailStatusEvent> sm = build(id);
        sendEvent(id, sm, EmailStatusEvent.SUCCESSFUL_CONFIRMATION);
    }

    @Override
    public void expiryConfirmation(UUID id) {
        StateMachine<EmailStatusEnum, EmailStatusEvent> sm = build(id);
        sendEvent(id, sm, EmailStatusEvent.EXPIRATION_CONFIRMATION);
    }

    private void sendEvent(UUID id, StateMachine<EmailStatusEnum, EmailStatusEvent> sm, EmailStatusEvent event) {
        Message<EmailStatusEvent> message = MessageBuilder.withPayload(event).setHeader(EMAIL_VERIFICATION_HEADER, id).build();
        sm.sendEvent(message);
    }

    private StateMachine<EmailStatusEnum, EmailStatusEvent> build(UUID id) {
        EmailVerification emailVerification = emailVerificationRepository.getReferenceById(id);

        StateMachine<EmailStatusEnum, EmailStatusEvent> sm = emailStatusStateMachineFactory
                .getStateMachine(emailVerification.getId().toString());
        sm.stop();
        sm.getStateMachineAccessor().doWithAllRegions(sma -> {
            sma.addStateMachineInterceptor(emailStatusInterceptor);
            sma.resetStateMachine(new DefaultStateMachineContext<>(emailVerification.getStatus(), null, null, null));
        });
        sm.start();
        return sm;
    }
}
