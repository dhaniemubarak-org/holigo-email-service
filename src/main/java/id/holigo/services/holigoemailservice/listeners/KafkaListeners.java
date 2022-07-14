package id.holigo.services.holigoemailservice.listeners;

import id.holigo.services.common.model.EmailDto;
import id.holigo.services.holigoemailservice.config.KafkaTopicConfig;
import id.holigo.services.holigoemailservice.services.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaListeners {

    private EmailService emailService;

    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = KafkaTopicConfig.EMAIL_VERIFICATION, groupId = "email", containerFactory = "emailListenerContainerFactory")
    void listener(EmailDto data) {
        log.info("Email listener is running...");
        log.info("Email -> {}", data.toString());
        emailService.sendEmail(data);
    }
}
