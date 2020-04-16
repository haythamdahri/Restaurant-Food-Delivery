package org.restaurant.salado.utils;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * @author Haytam DAHRI
 */
@Component
public class RestaurantUtils {

    @Autowired
    private static Environment environment;

    @Value("${HOSTNAME}")
    private static String HOSTNAME;

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
