package id.holigo.services.holigoemailservice.web.controllers;

import id.holigo.services.holigoemailservice.services.EmailService;
import id.holigo.services.holigoemailservice.web.model.EmailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class EmailController {

    private EmailService emailService;

    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/api/v1/email")
    public ResponseEntity<HttpStatus> sendEmail(@RequestBody EmailDto emailDto) {
        log.info("Call sendEmail");
        emailService.sendEmail(emailDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
