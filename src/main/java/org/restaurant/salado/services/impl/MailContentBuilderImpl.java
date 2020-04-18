package org.restaurant.salado.services.impl;

import org.restaurant.salado.services.MailContentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.Date;

/**
 * @author Haytam DAHRI
 */
@Service
public class MailContentBuilderImpl implements MailContentBuilder {

    private static final String TOKEN = "token";
    private static final String HOST = "host";
    private static final String PAYMENT_ID = "paymentId";
    private static final String TIMESTAMP = "timestamp";

    private SpringTemplateEngine templateEngine;

    @Value("${HOSTNAME}")
    private String hostname;

    @Autowired
    public void setTemplateEngine(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public String buildActivationEmail(String token) {
        Context context = new Context();
        context.setVariable(TOKEN, token);
        context.setVariable(HOST, this.hostname);
        return templateEngine.process("mailing/activation-mail", context);
    }

    @Override
    public String buildPasswordResetEmail(String token) {
        Context context = new Context();
        context.setVariable(TOKEN, token);
        context.setVariable(HOST, this.hostname);
        return templateEngine.process("mailing/password-reset", context);
    }

    @Override
    public String buildPasswordResetCompleteEmail() {
        Context context = new Context();
        context.setVariable(HOST, this.hostname);
        return templateEngine.process("mailing/password-reset-complete", context);
    }

    @Override
    public String buildUpdateUserMailEmail(String token) {
        Context context = new Context();
        context.setVariable(TOKEN, token);
        context.setVariable(HOST, this.hostname);
        return templateEngine.process("mailing/update-email-mail", context);
    }

    @Override
    public String buildPostChargeEmail(Long paymentId, Date timestamp) {
        Context context = new Context();
        context.setVariable(PAYMENT_ID, paymentId);
        context.setVariable(TIMESTAMP, timestamp);
        context.setVariable(HOST, this.hostname);
        return templateEngine.process("mailing/post-payment", context);
    }

}
