package org.restaurant.salado.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.restaurant.salado.entities.Payment;
import org.restaurant.salado.providers.Constants;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Haytham DAHRI
 */
@Component
@Aspect
public class PaymentAspect {

    /**
     * Delete payment file after creation to free up space
     *
     * @param joinPoint: JoinPoint object
     * @param result:    Returned object
     */
    @AfterReturning(pointcut = "execution(* org.restaurant.salado.services.impl.PaymentContentBuilderDevImpl.buildPaymentContent(..))", returning = "result")
    public void removeFileAfterReturningPaymentDetailsFile(JoinPoint joinPoint, Object result) throws IOException {
        // Retrieve parameters
        Object[] args = joinPoint.getArgs();
        Payment payment = (Payment) args[0];
        // Delete created file for payment
        Files.delete(Paths.get(Constants.PDF_OUTPUT_DIRECTORY.replace("ID", payment.getId().toString())));
    }

}
