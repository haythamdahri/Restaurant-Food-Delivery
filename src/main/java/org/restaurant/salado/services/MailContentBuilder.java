package org.restaurant.salado.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

public interface MailContentBuilder {

    public String buildActivationEmail(String token);

    public String buildPasswordResetEmail(String token);

    public String buildPasswordResetCompleteEmail(String token);

    public String buildUpdateUserMailEmail(String token);


}
