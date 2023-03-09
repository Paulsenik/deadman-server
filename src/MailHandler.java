
public class MailHandler {

    private String sender;
    private DeadmanManager manager;

    public MailHandler(String sender, DeadmanManager manager) {
        this.sender = sender;
        this.manager = manager;
    }

    public synchronized boolean sendMail(String receiver, String title, String message) {

        // TODO - replace with mail
        System.err.println("MAIL> " + sender + " -> " + receiver + " : " + title + "\n" + message);

        manager.log("[Mail] :: [sender=" + sender + "],[receiver" + receiver + "],[title=" + title + "] :: success");
        return true;
    }

}
