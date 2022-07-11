package id.holigo.services.holigoemailservice.listeners;

import id.holigo.services.common.EmailDto;
import id.holigo.services.holigoemailservice.config.KafkaTopicConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaListeners {
    @KafkaListener(topics = KafkaTopicConfig.EMAIL_CONFIRMATION, groupId = "email", containerFactory = "emailListenerContainerFactory")
    void listener(EmailDto data) {
        log.info("Kafka listener received : " + data.getTo());
    }
}
