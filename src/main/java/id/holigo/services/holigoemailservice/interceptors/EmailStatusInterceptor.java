package id.holigo.services.holigoemailservice.interceptors;

import id.holigo.services.common.model.EmailStatusEnum;
import id.holigo.services.holigoemailservice.domain.EmailVerification;
import id.holigo.services.holigoemailservice.events.EmailStatusEvent;
import id.holigo.services.holigoemailservice.repositories.EmailVerificationRepository;
import id.holigo.services.holigoemailservice.services.EmailStatusServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class EmailStatusInterceptor extends StateMachineInterceptorAdapter<EmailStatusEnum, EmailStatusEvent> {

    private final EmailVerificationRepository emailVerificationRepository;

    @Override
    public void preStateChange(State<EmailStatusEnum, EmailStatusEvent> state,
                               Message<EmailStatusEvent> message,
                               Transition<EmailStatusEnum, EmailStatusEvent> transition,
                               StateMachine<EmailStatusEnum, EmailStatusEvent> stateMachine,
                               StateMachine<EmailStatusEnum, EmailStatusEvent> rootStateMachine) {
        Optional.ofNullable(message).flatMap(msg -> Optional.of(
                UUID.fromString(Objects.requireNonNull(msg.getHeaders().get(EmailStatusServiceImpl.EMAIL_VERIFICATION_HEADER)).toString())
        )).ifPresent(id -> {
            EmailVerification emailVerification = emailVerificationRepository.getReferenceById(id);
            emailVerification.setStatus(state.getId());
            emailVerificationRepository.save(emailVerification);
        });
    }
}
