package id.holigo.services.holigoemailservice.web.controllers;

import id.holigo.services.common.model.EmailStatusEnum;
import id.holigo.services.holigoemailservice.domain.EmailVerification;
import id.holigo.services.holigoemailservice.repositories.EmailVerificationRepository;
import id.holigo.services.holigoemailservice.services.EmailStatusService;
import id.holigo.services.holigoemailservice.services.EmailVerificationService;
import id.holigo.services.holigoemailservice.web.mappers.EmailVerificationMapper;
import id.holigo.services.holigoemailservice.web.model.EmailVerificationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@RestController
public class EmailVerificationController {

    private static final String PATH = "/api/v1/emailVerifications";

    private EmailVerificationRepository emailVerificationRepository;

    private EmailVerificationMapper emailVerificationMapper;

    private EmailVerificationService emailVerificationService;

    private EmailStatusService emailStatusService;

    @Autowired
    public void setEmailStatusService(EmailStatusService emailStatusService) {
        this.emailStatusService = emailStatusService;
    }

    @Autowired
    public void setEmailVerificationService(EmailVerificationService emailVerificationService) {
        this.emailVerificationService = emailVerificationService;
    }

    @Autowired
    public void setEmailVerificationRepository(EmailVerificationRepository emailVerificationRepository) {
        this.emailVerificationRepository = emailVerificationRepository;
    }

    @Autowired
    public void setEmailVerificationMapper(EmailVerificationMapper emailVerificationMapper) {
        this.emailVerificationMapper = emailVerificationMapper;
    }

    @PostMapping(PATH)
    public ResponseEntity<HttpStatus> create(@RequestBody EmailVerificationDto emailVerificationDto,
                                             @RequestHeader("user-id") Long userId) {
        EmailVerification emailVerification = emailVerificationService.createEmail(emailVerificationDto, userId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(UriComponentsBuilder.fromPath(PATH + "/{id}").buildAndExpand(emailVerification.getId()).toUri());
        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @GetMapping(PATH + "/{id}")
    public ResponseEntity<EmailVerificationDto> show(@PathVariable("id") UUID id, @RequestHeader("user-id") Long userId) {

        Optional<EmailVerification> fetchEmailVerification = emailVerificationRepository.findById(id);
        if (fetchEmailVerification.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        EmailVerification emailVerification = fetchEmailVerification.get();
        if (!emailVerification.getUserId().equals(userId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(emailVerificationMapper
                .emailVerificationToEmailVerificationDto(emailVerification), HttpStatus.OK);
    }

    @GetMapping("emailVerifications")
    public ModelAndView verified(@RequestParam("verificationCode") String verificationCode) {
        Optional<EmailVerification> fetchEmailVerification = emailVerificationRepository.findByVerificationCodeAndStatus(
                verificationCode, EmailStatusEnum.WAITING_CONFIRMATION
        );
        fetchEmailVerification.ifPresent(emailVerification -> emailStatusService.successConfirmation(emailVerification.getId()));
        return new ModelAndView("landing-email-verifications");
    }
}
