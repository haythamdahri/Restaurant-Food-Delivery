package org.restaurant.salado.services.impl;

import org.restaurant.salado.entities.Payment;
import org.restaurant.salado.services.EmailService;
import org.restaurant.salado.services.MailContentBuilder;
import org.restaurant.salado.utils.RestaurantUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * @author Haytam DAHRI
 */
@Service
@Async
public class EmailServiceImpl implements EmailService {

    @Autowired
    private MailContentBuilder mailContentBuilder;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${mail.sender}")
    private String from;

    /**
     * Send simple email message implementation
     * @param to
     * @param subject
     * @param text
     * @return CompletableFuture<Boolean>
     */
    @Override
    public CompletableFuture<Boolean> sendSimpleMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(this.from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            this.mailSender.send(message);
            return CompletableFuture.completedFuture(true);
        } catch (Exception ex) {
            return CompletableFuture.completedFuture(false);
        }
    }

    /**
     * Send professional email message with template implementation
     * @param token
     * @param to
     * @param subject
     * @return CompletableFuture<Boolean>
     */
    @Override
    public CompletableFuture<Boolean> sendActivationEmail(String token, String to, String subject) {
        try {
            MimeMessage message = this.mailSender.createMimeMessage();
            String templateText = this.mailContentBuilder.buildActivationEmail(token);
            MimeMessageHelper helper = RestaurantUtils.buildMimeMessageHelper(this.from, to, subject, templateText, message, true);
            this.mailSender.send(helper.getMimeMessage());
            return CompletableFuture.completedFuture(true);
        } catch (Exception ex) {
            return CompletableFuture.completedFuture(false);
        }
    }

    /**
     * Send professional email message with template implementation
     * @param token
     * @param to
     * @param subject
     * @return CompletableFuture<Boolean>
     */
    @Override
    public CompletableFuture<Boolean> sendResetPasswordEmail(String token, String to, String subject) {
        try {
            MimeMessage message = this.mailSender.createMimeMessage();
            String templateText = this.mailContentBuilder.buildPasswordResetEmail(token);
            MimeMessageHelper helper = RestaurantUtils.buildMimeMessageHelper(this.from, to, subject, templateText, message, true);
            this.mailSender.send(helper.getMimeMessage());
            return CompletableFuture.completedFuture(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            return CompletableFuture.completedFuture(false);
        }
    }

    /**
     * Send professional email message with template implementation
     * @param to
     * @param subject
     * @return CompletableFuture<Boolean>
     */
    @Override
    public CompletableFuture<Boolean> sendResetPasswordCompleteEmail(String to, String subject) {
        try {
            MimeMessage message = this.mailSender.createMimeMessage();
            String templateText = this.mailContentBuilder.buildPasswordResetCompleteEmail();
            MimeMessageHelper helper = RestaurantUtils.buildMimeMessageHelper(this.from, to, subject, templateText, message, true);
            this.mailSender.send(helper.getMimeMessage());
            return CompletableFuture.completedFuture(true);
        } catch (Exception ex) {
            return CompletableFuture.completedFuture(false);
        }
    }

    /**
     * Send user email update
     * Provide update token
     * @param token
     * @param to
     * @param subject
     * @return CompletableFuture<Boolean>
     */
    @Override
    public CompletableFuture<Boolean> sendUpdateUserMailEmail(String token, String to, String subject) {
        try {
            MimeMessage message = this.mailSender.createMimeMessage();
            String templateText = this.mailContentBuilder.buildUpdateUserMailEmail(token);
            MimeMessageHelper helper = RestaurantUtils.buildMimeMessageHelper(this.from, to, subject, templateText, message, true);
            this.mailSender.send(helper.getMimeMessage());
            return CompletableFuture.completedFuture(true);
        } catch (Exception ex) {
            return CompletableFuture.completedFuture(false);
        }
    }

    /**
     * Send post email to user
     * @param to
     * @param subject
     * @param paymentId
     * @param timestamp
     * @return CompletableFuture<Boolean>
     */
    @Override
    public CompletableFuture<Boolean> sendPostPaymentEmail(String to, String subject, Long paymentId, Date timestamp) {
        try {
            MimeMessage message = this.mailSender.createMimeMessage();
            String templateText = this.mailContentBuilder.buildPostChargeEmail(paymentId, timestamp);
            MimeMessageHelper helper = RestaurantUtils.buildMimeMessageHelper(this.from, to, subject, templateText, message, true);
            this.mailSender.send(helper.getMimeMessage());
            return CompletableFuture.completedFuture(true);
        } catch (Exception ex) {
            return CompletableFuture.completedFuture(false);
        }
    }

    /**
     * Send email with attachment implementation
     * @param to
     * @param subject
     * @param text
     * @param pathToAttachment
     * @return CompletableFuture<Boolean>
     */
    @Override
    public CompletableFuture<Boolean> sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment) {
        try {
            MimeMessage message = this.mailSender.createMimeMessage();
            MimeMessageHelper helper = RestaurantUtils.buildMimeMessageHelper(this.from, to, subject, text, message, true);
            FileSystemResource file
                    = new FileSystemResource(new File(pathToAttachment));
            // Add file attachment
            helper.addAttachment(Objects.requireNonNull(file.getFilename()), file);
            mailSender.send(message);
            return CompletableFuture.completedFuture(true);
        } catch (Exception ex) {
            return CompletableFuture.completedFuture(false);
        }
    }


}
