package id.holigo.services.holigoemailservice.services;

import id.holigo.services.common.model.EmailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    private JavaMailSender javaMailSender;

    @Autowired
    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    private SpringTemplateEngine thymeleafTemplateEngine;

    @Autowired
    public void setThymeleafTemplateEngine(SpringTemplateEngine thymeleafTemplateEngine) {
        this.thymeleafTemplateEngine = thymeleafTemplateEngine;
    }

    @Override
    public void sendEmail(EmailDto emailDto) throws MessagingException {
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(emailDto.getData());
        String htmlBody = thymeleafTemplateEngine.process(emailDto.getTemplate() + ".html", thymeleafContext);
        sendHtmlMessage(emailDto.getTo(), emailDto.getFrom(), emailDto.getSubject(), htmlBody);
    }

    private void sendHtmlMessage(String to, String from, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setFrom(from);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        javaMailSender.send(message);
    }

    @Override
    public void sendSimpleMail(EmailDto emailDto) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(emailDto.getTo());
        simpleMailMessage.setFrom(emailDto.getFrom());
        simpleMailMessage.setSubject(emailDto.getSubject());
        simpleMailMessage.setText(emailDto.getData().toString());
        javaMailSender.send(simpleMailMessage);
    }


}
