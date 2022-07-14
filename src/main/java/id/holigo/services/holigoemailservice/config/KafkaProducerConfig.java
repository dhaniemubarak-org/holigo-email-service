package id.holigo.services.holigoemailservice.config;

import id.holigo.services.common.model.EmailDto;
import id.holigo.services.common.model.UpdateUserEmailStatusDto;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value(("${spring.kafka.bootstrap-servers}"))
    private String bootstrapServers;

    public Map<String, Object> producerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, EmailDto> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public ProducerFactory<String, UpdateUserEmailStatusDto> updateUserProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public KafkaTemplate<String, UpdateUserEmailStatusDto> updateUserKafkaTemplate(
            ProducerFactory<String, UpdateUserEmailStatusDto> updateUserProducerFactory) {
        return new KafkaTemplate<>(updateUserProducerFactory);
    }

    @Bean
    public KafkaTemplate<String, EmailDto> kafkaTemplate(ProducerFactory<String, EmailDto> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

}
