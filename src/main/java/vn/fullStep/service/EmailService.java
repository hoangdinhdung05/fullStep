package vn.fullStep.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j(topic = "Email_Service")
public class EmailService {

    @Autowired
    private SendGrid sendGrid;

    @Value("${spring.sendgrid.fromEmail}")
    private String fromEmail;

    @Value("${spring.sendgrid.templateId}")
    private String templateId;

    @Value("${spring.sendgrid.verificationLink}")
    private String verificationLink;

    /**
     * Gửi email sử dụng SendGrid
     * @param toEmail
     * @param subject
     * @param body
     */
    public void sendEmail(String toEmail, String subject, String body) {
        Email from = new Email(fromEmail); // Email của bạn
        Email to = new Email(toEmail);

        Content content = new Content("text/plain", body);
        Mail mail = new Mail(from, subject, to, content);

        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);

            // Check the response status code
            if (response.getStatusCode() == 202) {
                log.info("Email sent successfully to {}", toEmail);
            } else {
                log.error("Failed to send email. Status code: {}, Body: {}", response.getStatusCode(), response.getBody());
            }

        } catch (IOException e) {
            log.error("Error sending email: {}", e.getMessage(), e);
//            return "Error sending email: " + e.getMessage();
        }
    }

    /**
     * Send email with HTML content using SendGrid
     * @param toEmail
     * @param name
     * throws IOException
     */
    public void sendVerificationEmail(String toEmail, String name) throws IOException {
        log.info("Sending verification email to: {}", toEmail);

        Email email = new Email(fromEmail, "Hoang Dung");
        Email to = new Email(toEmail);

        String subject = "Xác thực tài khoản của bạn";

        String secretCode = String.format("?secretCode=%s", UUID.randomUUID()); // Replace with your actual verification link

        //TODO generate secret code and save it to the database

        // HTML content for the email
        Map<String, String> map = new HashMap<>();

        map.put("name", name);
        map.put("verification_link", verificationLink + secretCode);

        Mail mail = new Mail();
        mail.setFrom(email);
        mail.setSubject(subject);

        Personalization personalization = new Personalization();
        personalization.addTo(to);

        // Set dynamic template data
        map.forEach(personalization::addDynamicTemplateData);
        mail.addPersonalization(personalization);
        mail.setTemplateId(templateId); // Replace with your SendGrid template ID

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sendGrid.api(request);
        if(response.getStatusCode() == 202) {
            log.info("Verification email sent successfully to {}", toEmail);
        } else {
            log.error("Failed to send verification email. Status code: {}, Body: {}", response.getStatusCode(), response.getBody());
        }
    }
}