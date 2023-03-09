
public class MailHandler {

    private String sender;

    public MailHandler(String sender) {
        this.sender = sender;
    }

    public synchronized boolean sendMail(String receiver, String title, String message) {

        System.err.println("MAIL> " + sender + " -> " + receiver + " : " + title + "\n" + message);

        return true;
    }

}
