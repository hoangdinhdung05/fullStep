package vn.fullStep.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j(topic = "Email_Service")
public class EmailService {

    @Autowired
    private SendGrid sendGrid;

    @Value("${spring.sendgrid.fromEmail}")
    private String fromEmail;

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

            // Kiểm tra kết quả phản hồi từ SendGrid
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
}