package org.restaurant.salado.services;

import java.util.Map;

public interface EmailService {

    boolean sendSimpleMessage(String to, String subject, String text);

    boolean sendActivationEmail(String token, String to, String subject);

    boolean sendResetPasswordEmail(String token, String to, String subject);

    boolean sendResetPasswordCompleteEmail(String to, String subject);

    boolean sendUpdateUserMailEmail(String token, String to, String subject);

    boolean sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment);

}
