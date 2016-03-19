package utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by Leonti on 2016-03-17.
 */
public class MailUtil {
    private static void sendMail(String to, String from,
                                 String subject, String body,
                                 boolean bodyIsHTML)
            throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.host", "localhost");
        properties.put("mail.smtp.port", 25);
        Session session = Session.getDefaultInstance(properties);
        session.setDebug(true);

        Message message = new MimeMessage(session);
        message.setSubject(subject);
        if (bodyIsHTML) {
            message.setContent(body, "text/html");
        } else {
            message.setText(body);
        }

        Address fromAddress = new InternetAddress(from);
        message.setFrom(fromAddress);

        Address toAddress = new InternetAddress(to);
//        Address toAddress = new InternetAddress(to, "name");

        message.setRecipient(Message.RecipientType.TO, toAddress);
//        message.setRecipient(Message.RecipientType.CC, toAddress);
//        message.setRecipient(Message.RecipientType.BCC, toAddress);
//        message.addRecipient(Message.RecipientType.TO, toAddress);

//        Address[] addresses = {new InternetAddress(to)};
//        message.setRecipients(Message.RecipientType.TO, addresses);

        Transport.send(message);

//        Transport transport = session.getTransport();
//        transport.connect("someemail@gmail.com", "password");
//        transport.sendMessage(message, message.getAllRecipients());
//        transport.close();

    }
}
