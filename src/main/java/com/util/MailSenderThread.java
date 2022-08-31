package com.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

public class MailSenderThread extends Thread {
    private static Logger logger = LogManager.getLogger();
    private static final String MAIL_PROPERTIES_PATH = "mail.properties";
    private static final String HOST = "mail.smtps.host";
    private static final String PORT = "mail.smtp.port";
    private static final String USER = "smtps.auth.user";
    private static final String PASSWORD = "smtps.auth.pass";
    private static final String SENDER_NAME = "mail.sender.name";
    private static final String CONTENT_VALUE = "text/html; charset=UTF-8";
    private String mailTo;
    private String mailSubject;
    private String mailContent;
    private Session mailSession;
    private Properties properties;

    public MailSenderThread(String mailTo, String mailSubject, String mailContent) {
        this.mailTo = mailTo;
        this.mailSubject = mailSubject;
        this.mailContent = mailContent;
    }

    private void buildMailSession() {
        properties = new Properties();
        try {
            properties.load(MailSenderThread.class.getClassLoader().
                    getResourceAsStream(MAIL_PROPERTIES_PATH));
        } catch (IOException e) {
            logger.log(Level.ERROR, "Unable to find mail properties file");
        }

        mailSession = Session.getDefaultInstance(properties);
        mailSession.setDebug(true);
    }

    @Override
    public void run() {
        try {
            buildMailSession();
            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(properties.getProperty(USER),
                    String.valueOf(properties.get(SENDER_NAME))));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
            message.setSubject(mailSubject);
            message.setContent(mailContent, CONTENT_VALUE);
            message.setSentDate(new Date());
            Transport transport = mailSession.getTransport();
            transport.connect(properties.getProperty(HOST),
                    Integer.parseInt(properties.getProperty(PORT)),
                    properties.getProperty(USER),
                    properties.getProperty(PASSWORD));
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();
            logger.log(Level.INFO, "Message sent");
        } catch (MessagingException | UnsupportedEncodingException e) {
            logger.log(Level.ERROR, e);
        }
    }
}
