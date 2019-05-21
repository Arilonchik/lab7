import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class EmailSender {
    public String sendemail(String em, String pas){
        try {
            final Properties properties = new Properties();
            properties.setProperty("mail.transport.protocol","smtps");
            properties.setProperty("mail.smtps.host", "smtp.gmail.com"); // "smtp.gmail.com"
            properties.setProperty("mail.smtps.user", "loviparol@gmail.com");
            // properties.setProperty("mail.smtps.port", "1705");
            //properties.setProperty("mail.smtp.socketFactory.port", "1707");
            //properties.load(new FileInputStream("mail.properties"));

            Session mailSession = Session.getDefaultInstance(properties);
            MimeMessage message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress("loviparol@gmail.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(em));
            message.setSubject("Pass");
            message.setText("Hello dear Alexey Evren'evich, this is ur password, don't tell it anybody\n" + pas);



            Transport tr = mailSession.getTransport();
            tr.connect("loviparol@gmail.com", "e13112000");
            tr.sendMessage(message, message.getAllRecipients());
            tr.close();
            return "done";
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return "Und";
    }

}
