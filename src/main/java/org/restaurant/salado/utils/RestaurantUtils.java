package org.restaurant.salado.utils;

import org.apache.commons.io.FilenameUtils;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author Haytam DAHRI
 */
public class RestaurantUtils {

    /**
     * etrieve file extension from a file name
     *
     * @param filename
     * @return String
     */
    public static String getExtensionByApacheCommonLib(String filename) {
        return FilenameUtils.getExtension(filename);
    }

    /**
     * Mime message builder
     *
     * @param from
     * @param to
     * @param subject
     * @param text
     * @param message
     * @param isMultipart
     * @return MimeMessageHelper
     * @throws MessagingException
     */
    public static MimeMessageHelper buildMimeMessageHelper(String from, String to, String subject, String text, MimeMessage message, boolean isMultipart) throws MessagingException {
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, isMultipart);
        // return built helper
        return helper;
    }

}
