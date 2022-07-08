package id.holigo.services.holigoemailservice;

import id.holigo.services.common.EmailDto;
import id.holigo.services.holigoemailservice.config.KafkaTopicConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class HoligoEmailServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HoligoEmailServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(KafkaTemplate<String, EmailDto> kafkaTemplate) {
        EmailDto emailDto = new EmailDto();
        emailDto.setTo("dhaniemubarak@gmail.com");
        emailDto.setFrom("noreply@holigo.id");
        emailDto.setSubject("Coba Kafka");
        emailDto.setContent("Ini uji coba dengan kafka");
        return args -> kafkaTemplate.send(KafkaTopicConfig.EMAIL_CONFIRMATION, emailDto);
    }

}
