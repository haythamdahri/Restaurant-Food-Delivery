package org.restaurant.salado.services;

import java.util.Map;

public interface EmailService {

    public boolean sendSimpleMessage(String to, String subject, String text);

    public boolean sendActivationEmail(String token, String to, String subject);

    public boolean sendResetPasswordEmail(String token, String to, String subject);

    public boolean sendResetPasswordCompleteEmail(String token, String to, String subject);

    public boolean sendUpdateUserMailEmail(String token, String to, String subject);

    public boolean sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment);

}
