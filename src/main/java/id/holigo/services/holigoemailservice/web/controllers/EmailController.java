package id.holigo.services.holigoemailservice.web.controllers;

import id.holigo.services.holigoemailservice.config.KafkaTopicConfig;
import id.holigo.services.common.model.EmailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class EmailController {

    private KafkaTemplate<String, EmailDto> emailKafkaTemplate;

    @Autowired
    public void setEmailKafkaTemplate(KafkaTemplate<String, EmailDto> emailKafkaTemplate) {
        this.emailKafkaTemplate = emailKafkaTemplate;
    }

    @PostMapping("/api/v1/email")
    public ResponseEntity<HttpStatus> sendEmail(@RequestBody EmailDto emailDto) {
        log.info("Call sendEmail");
        emailKafkaTemplate.send(KafkaTopicConfig.EMAIL_VERIFICATION, emailDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
