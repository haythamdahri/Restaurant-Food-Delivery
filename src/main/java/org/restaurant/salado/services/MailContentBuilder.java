package org.restaurant.salado.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

public interface MailContentBuilder {

    String buildActivationEmail(String token);

    String buildPasswordResetEmail(String token);

    String buildPasswordResetCompleteEmail();

    String buildUpdateUserMailEmail(String token);

}
