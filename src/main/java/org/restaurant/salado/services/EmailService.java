package org.restaurant.salado.services;

import java.time.LocalDate;
import java.util.Date;

/**
 * @author Haytam DAHRI
 */
public interface EmailService {

    boolean sendSimpleMessage(String to, String subject, String text);

    boolean sendActivationEmail(String token, String to, String subject);

    boolean sendResetPasswordEmail(String token, String to, String subject);

    boolean sendResetPasswordCompleteEmail(String to, String subject);

    boolean sendPostPaymentEmail(String to, String subject, Long paymentId, Date timestamp);

    boolean sendUpdateUserMailEmail(String token, String to, String subject);

    boolean sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment);

}
