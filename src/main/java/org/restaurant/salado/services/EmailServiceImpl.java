package org.restaurant.salado.services;

import org.restaurant.salado.utils.RestaurantUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailServiceImpl implements EmailService {

    /*
     * @Inject an instance of MailContentBuilder
     */
    @Autowired
    private MailContentBuilder mailContentBuilder;

    /*
     * @Inject an instance of JavaMailSender which we have created as a bean
     */
    @Autowired
    private JavaMailSender mailSender;

    /**
     * Inject from mail sender
     */
    @Value("${mail.sender}")
    private String from;

    /**
     * Inject restaurant utils
     */
    @Autowired
    private RestaurantUtils restaurantUtils;

    /*
     * @Send simple email message implementation
     */
    @Override
    public boolean sendSimpleMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(this.from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            this.mailSender.send(message);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /*
     * @Send professional templated email message implementation
     */
    @Override
    public boolean sendActivationEmail(String token, String to, String subject) {
        try {
            MimeMessage message = this.mailSender.createMimeMessage();
            String templatedText = this.mailContentBuilder.buildActivationEmail(token);
            MimeMessageHelper helper = this.restaurantUtils.buildMimeMessageHelper(this.from, to, subject, templatedText, message, true);
            this.mailSender.send(helper.getMimeMessage());
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /*
     * @Send professional templated email message implementation
     */
    @Override
    public boolean sendResetPasswordEmail(String token, String to, String subject) {
        try {
            MimeMessage message = this.mailSender.createMimeMessage();
            String templatedText = this.mailContentBuilder.buildPasswordResetEmail(token);
            MimeMessageHelper helper = this.restaurantUtils.buildMimeMessageHelper(this.from, to, subject, templatedText, message, true);
            this.mailSender.send(helper.getMimeMessage());
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /*
     * @Send professional templated email message implementation
     */
    @Override
    public boolean sendResetPasswordCompleteEmail(String to, String subject) {
        try {
            MimeMessage message = this.mailSender.createMimeMessage();
            String templatedText = this.mailContentBuilder.buildPasswordResetCompleteEmail();
            MimeMessageHelper helper = this.restaurantUtils.buildMimeMessageHelper(this.from, to, subject, templatedText, message, true);
            this.mailSender.send(helper.getMimeMessage());
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /*
     * @Send email update
     */
    @Override
    public boolean sendUpdateUserMailEmail(String token, String to, String subject) {
        try {
            MimeMessage message = this.mailSender.createMimeMessage();
            String templatedText = this.mailContentBuilder.buildUpdateUserMailEmail(token);
            MimeMessageHelper helper = this.restaurantUtils.buildMimeMessageHelper(this.from, to, subject, templatedText, message, true);
            this.mailSender.send(helper.getMimeMessage());
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /*
     * @Send email with attachment implementation
     */
    @Override
    public boolean sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment) {
        try {
            MimeMessage message = this.mailSender.createMimeMessage();

            MimeMessageHelper helper = this.restaurantUtils.buildMimeMessageHelper(this.from, to, subject, text, message, true);
            FileSystemResource file
                    = new FileSystemResource(new File(pathToAttachment));
            // Add file attachment
            helper.addAttachment(file.getFilename(), file);
            mailSender.send(message);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }


}
