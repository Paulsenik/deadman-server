import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MailHandler {

    private final String username; // sending mail Address
    private final String password;
    private final String hostname;
    private final String smtpPort;

    private Thread queueHandler;

    /**
     * Automatically gets the credentials from the secrets.json
     */
    public MailHandler() {
        try {
            Reader reader = Files.newBufferedReader(Paths.get("secrets.json"));
            JsonObject parser = (JsonObject) Jsoner.deserialize(reader);
            username = (String) parser.get("username");
            password = (String) parser.get("password");
            hostname = (String) parser.get("hostname");
            smtpPort = (String) parser.get("smtpPort");
        } catch (IOException | JsonException e) {
            System.err.println("There was a problem reading the mail-secrets");
            throw new RuntimeException(e);
        }
        initMailThread();
    }

    public MailHandler(String username, String password, String hostname, String smtpPort) {
        this.username = username;
        this.password = password;
        this.hostname = hostname;
        this.smtpPort = smtpPort;
        initMailThread();
    }

    private void initMailThread() {
        queueHandler = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    // TODO
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        queueHandler.start();
    }

    public synchronized boolean queueMail(String receiver, String subject, String message) {
        try {
            System.err.println("MAIL> " + username + " -> " + receiver + " : " + subject + "\n" + message);

            DeadmanManager.instance.log("[Mail] :: [sender=" + username + "],[receiver=" + receiver + "],[title=" + subject + "] :: delivered successfully!");
            return true;
        } catch (RuntimeException e) {
            DeadmanManager.instance.log("[Mail] :: [sender=" + username + "],[receiver=" + receiver + "],[title=" + subject + "] :: could not be delivered!");
            return false;
        }
    }

    private synchronized boolean sendMail(String receiver, String subject, String message) {

        // Create The Email
        MultiPartEmail email = new MultiPartEmail();
        try {
            email.setHostName(hostname);
            email.setSmtpPort(Integer.parseInt(smtpPort));
            email.setAuthentication(username, password);
            email.setFrom(username);
            email.addTo(receiver);
            email.setMsg(message);
            email.setSSLOnConnect(true);
            email.setStartTLSEnabled(true);
            email.setSubject(subject);
            //email.attach(attachment);
            email.send();

        } catch (NumberFormatException e) {
            System.err.println("SMTP-Port must be a number!");
            return false;
        } catch (EmailException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void main(String[] args) throws IOException, JsonException {


    }

}
