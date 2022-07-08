package id.holigo.services.holigoemailservice.listeners;

import id.holigo.services.common.EmailDto;
import id.holigo.services.holigoemailservice.config.KafkaTopicConfig;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    @KafkaListener(topics = KafkaTopicConfig.EMAIL_CONFIRMATION, groupId = "email")
    void listener(String data) {
//        try {
//            EmailDto emailDto = new ObjectMapper().readValue(data, EmailDto.class);
        System.out.println("Listener received: " + data);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
    }
}
