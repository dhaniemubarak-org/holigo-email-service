package id.holigo.services.holigoemailservice;

import id.holigo.services.common.EmailDto;
import id.holigo.services.holigoemailservice.config.KafkaTopicConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.NonNullApi;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@SpringBootApplication
public class HoligoEmailServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HoligoEmailServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(KafkaTemplate<String, EmailDto> kafkaTemplate) {
        return args -> {
            EmailDto emailDto = EmailDto.builder().to("dhaniemubarak@gmail.com").build();
            ListenableFuture<SendResult<String, EmailDto>> future = kafkaTemplate.send(KafkaTopicConfig.EMAIL_CONFIRMATION, emailDto);
            future.addCallback(new ListenableFutureCallback<>() {
                @Override
                public void onFailure(Throwable ex) {
                }

                @Override
                public void onSuccess(SendResult<String, EmailDto> result) {
                    log.info("Kafka sent " + result.getProducerRecord());
                }
            });
        };
    }

}
