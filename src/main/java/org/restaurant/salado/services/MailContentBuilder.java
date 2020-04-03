package org.restaurant.salado.services;

public interface MailContentBuilder {

    String buildActivationEmail(String token);

    String buildPasswordResetEmail(String token);

    String buildPasswordResetCompleteEmail();

    String buildUpdateUserMailEmail(String token);

}
