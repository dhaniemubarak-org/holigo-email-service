package id.holigo.services.holigoemailservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String EMAIL_VERIFICATION = "email-verification";

    public static final String USER_UPDATE_EMAIL_STATUS = "user-update-email-status";

    @Bean
    public NewTopic userUpdateEmailStatus() {
        return TopicBuilder.name(USER_UPDATE_EMAIL_STATUS).build();
    }

    @Bean
    public NewTopic emailConfirmationTopic() {
        return TopicBuilder.name(EMAIL_VERIFICATION).build();
    }
}
