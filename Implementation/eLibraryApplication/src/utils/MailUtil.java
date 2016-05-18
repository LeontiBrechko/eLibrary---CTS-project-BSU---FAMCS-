package utils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.Date;
import java.util.Properties;

/**
 * Created by Leonti on 2016-03-17.
 */
public class MailUtil {
    public static void sendDownloadZip(String toEmail, String toName, String fromEmail,
                                       String subject, String downloadFilePath)
            throws MessagingException, UnsupportedEncodingException {
        Session session = createGMailSMPTSession();

        Message message = new MimeMessage(session);
        message.setSubject(subject);
        message.setSentDate(Date.from(Instant.now()));
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail, toName));

        BodyPart messageBody = new MimeBodyPart();
        Multipart multipart = new MimeMultipart();
        DataSource source = new FileDataSource(downloadFilePath);
        messageBody.setText("Thank you for using our eLibrary!\n Here is your books!\n");
        multipart.addBodyPart(messageBody);
        messageBody = new MimeBodyPart();
        messageBody.setDataHandler(new DataHandler(source));
        messageBody.setFileName("Books.zip");
        multipart.addBodyPart(messageBody);
        message.setContent(multipart);

//        if (bodyIsHTML) {
//            message.setContent(body, "text/html");
//        } else {
//            message.setText(body);
//        }

//        message.setRecipient(Message.RecipientType.CC, toAddress);
//        message.setRecipient(Message.RecipientType.BCC, toAddress);
//        message.addRecipient(Message.RecipientType.TO, toAddress);

//        Address[] addresses = {new InternetAddress(to)};
//        message.setRecipients(Message.RecipientType.TO, addresses);

//        Transport.send(message);

        Transport transport = session.getTransport("smtps");
        transport.connect("smtp.gmail.com", "elibraryprojectfamcs@gmail.com", "theBestFaculty!");
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();

    }

    public static void sendConfirmationEmail(String toEmail, String toName,
                                              String fromEmail, String subject, String confirmationLink)
            throws MessagingException, UnsupportedEncodingException {
        Session session = createGMailSMPTSession();

        Message message = new MimeMessage(session);
        message.setSubject(subject);
        message.setSentDate(Date.from(Instant.now()));
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail, toName));
        message.setText("Please, use link provided below to activate your account!\n" +
                "Thank you for using our services!\n" + confirmationLink);

        Transport transport = session.getTransport("smtps");
        transport.connect("smtp.gmail.com", "elibraryprojectfamcs@gmail.com", "theBestFaculty!");
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    private static Session createGMailSMPTSession() {
        Properties properties = System.getProperties();
        properties.setProperty("mail.transport.protocol", "smtps");
        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.port", "465");
        properties.setProperty("mail.smtps.auth", "true");
        properties.put("mail.smtps.quitwait", "false");

        Session session = Session.getInstance(properties, null);
        session.setDebug(false);


        return session;
    }
}
