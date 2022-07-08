package id.holigo.services.holigoemailservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String EMAIL_CONFIRMATION = "email-confirmation";

    @Bean
    public NewTopic emailConfirmationTopic() {
        return TopicBuilder.name(EMAIL_CONFIRMATION).build();
    }
}
