package org.restaurant.salado.services.impl;

import com.itextpdf.html2pdf.HtmlConverter;
import org.restaurant.salado.entities.Payment;
import org.restaurant.salado.providers.Constants;
import org.restaurant.salado.services.PaymentContentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Haytham DAHRI
 */
@Service
@Profile("dev")
public class PaymentContentBuilderDevImpl implements PaymentContentBuilder {

    private static final String PAYMENT = "payment";
    private static final String HOST = "host";

    private SpringTemplateEngine templateEngine;

    @Value("${HOSTNAME}")
    private String hostname;

    @Autowired
    public void setTemplateEngine(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public InputStreamResource buildPaymentContent(Payment payment) throws IOException {
        Context context = new Context();
        context.setVariable(PAYMENT, payment);
        context.setVariable(HOST, this.hostname);
        String htmlPaymentContent = templateEngine.process("payments/payment-details", context);
        // Create pdf file and return output stream
        HtmlConverter.convertToPdf(htmlPaymentContent, new FileOutputStream(Constants.PDF_OUTPUT_DIRECTORY.replace("ID", payment.getId().toString())));
        return new InputStreamResource(new FileInputStream(Constants.PDF_OUTPUT_DIRECTORY.replace("ID", payment.getId().toString())));
    }
}
