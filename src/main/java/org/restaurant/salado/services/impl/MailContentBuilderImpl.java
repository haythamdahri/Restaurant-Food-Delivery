package org.restaurant.salado.services.impl;

import org.restaurant.salado.services.MailContentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;

/**
 * @author Haytam DAHRI
 */
@Service
public class MailContentBuilderImpl implements MailContentBuilder {

    private TemplateEngine templateEngine;

    @Autowired
    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public String buildActivationEmail(String token) {
        Context context = new Context();
        context.setVariable("token", token);
        return templateEngine.process("mailing/activation-mail", context);
    }

    @Override
    public String buildPasswordResetEmail(String token) {
        Context context = new Context();
        context.setVariable("token", token);
        return templateEngine.process("mailing/password-reset", context);
    }

    @Override
    public String buildPasswordResetCompleteEmail() {
        Context context = new Context();
        return templateEngine.process("mailing/password-reset-complete", context);
    }

    @Override
    public String buildUpdateUserMailEmail(String token) {
        Context context = new Context();
        context.setVariable("token", token);
        return templateEngine.process("mailing/update-email-mail", context);
    }

    @Override
    public String buildPostChargeEmail(Long paymentId, Date timestamp) {
        Context context = new Context();
        context.setVariable("paymentId", paymentId);
        context.setVariable("timestamp", timestamp);
        return templateEngine.process("mailing/post-payment", context);
    }

}
