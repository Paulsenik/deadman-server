import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class MailHandler {

    private final String sender;

    public MailHandler(String sender) {
        this.sender = sender;
    }

    public synchronized boolean sendMail(String receiver, String title, String message) {
        try {

            // TODO - replace with mail
            System.err.println("MAIL> " + sender + " -> " + receiver + " : " + title + "\n" + message);

            DeadmanManager.instance.log("[Mail] :: [sender=" + sender + "],[receiver=" + receiver + "],[title=" + title + "] :: delivered successfully!");
            return true;
        } catch (RuntimeException e) {
            DeadmanManager.instance.log("[Mail] :: [sender=" + sender + "],[receiver=" + receiver + "],[title=" + title + "] :: could not be delivered!");
            return false;
        }
    }

    public static void main(String[] args) throws IOException, JsonException, MessagingException {

        // https://www.baeldung.com/java-email

        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.derbabbo.de");
        prop.put("mail.smtp.ssl.trust", "derbabbo.de");

        Reader reader = Files.newBufferedReader(Paths.get("secrets.json"));
        JsonObject parser = (JsonObject) Jsoner.deserialize(reader);

        System.out.println("starting auth auth");

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication((String) parser.get("username"), (String) parser.get("password"));
            }
        });

        System.out.println("finished auth");

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress((String) parser.get("username")));
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse("test@paulsen.ooo"));
        message.setSubject("Mail Subject");

        String msg = "This is my first email using JavaMailer";

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        System.out.println("sending..");

        Transport.send(message);

    }

}
