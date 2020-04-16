package org.restaurant.salado.services.impl;

import org.restaurant.salado.services.MailContentBuilder;
import org.restaurant.salado.utils.RestaurantUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author Haytam DAHRI
 */
@Service
public class MailContentBuilderImpl implements MailContentBuilder {

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Value("${HOSTNAME}")
    private String hostname;

    @Override
    public String buildActivationEmail(String token) {
        Context context = new Context();
        context.setVariable("token", token);
        context.setVariable("host", this.hostname);
        return templateEngine.process("mailing/activation-mail", context);
    }

    @Override
    public String buildPasswordResetEmail(String token) {
        Context context = new Context();
        context.setVariable("token", token);
        context.setVariable("host", this.hostname);
        return templateEngine.process("mailing/password-reset", context);
    }

    @Override
    public String buildPasswordResetCompleteEmail() {
        Context context = new Context();
        context.setVariable("host", this.hostname);
        return templateEngine.process("mailing/password-reset-complete", context);
    }

    @Override
    public String buildUpdateUserMailEmail(String token) {
        Context context = new Context();
        context.setVariable("token", token);
        context.setVariable("host", this.hostname);
        return templateEngine.process("mailing/update-email-mail", context);
    }

    @Override
    public String buildPostChargeEmail(Long paymentId, Date timestamp) {
        Context context = new Context();
        context.setVariable("paymentId", paymentId);
        context.setVariable("timestamp", timestamp);
        context.setVariable("host", this.hostname);
        return templateEngine.process("mailing/post-payment", context);
    }

}
