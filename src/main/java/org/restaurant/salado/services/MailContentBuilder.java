package org.restaurant.salado.services;

import java.util.Date;

/**
 * @author Haytam DAHRI
 */
public interface MailContentBuilder {

    String buildActivationEmail(String token);

    String buildPasswordResetEmail(String token);

    String buildPasswordResetCompleteEmail();

    String buildUpdateUserMailEmail(String token);

    String buildPostChargeEmail(Long paymentId, Date timestamp);

}
