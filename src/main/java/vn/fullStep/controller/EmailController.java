package vn.fullStep.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.fullStep.service.EmailService;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "Email_Controller")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/send-email")
    public void sendEmail(@RequestParam String toEmail, @RequestParam String subject, @RequestParam String body) {
        log.info("Sending email to: {}, Subject: {}, Body: {}", toEmail, subject, body);
        emailService.sendEmail(toEmail, subject, body);
        log.info("Email sent to: {}", toEmail);
    }
}