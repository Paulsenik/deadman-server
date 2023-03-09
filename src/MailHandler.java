
public class MailHandler {

    private final String sender;
    private final DeadmanManager manager;

    public MailHandler(String sender, DeadmanManager manager) {
        this.sender = sender;
        this.manager = manager;
    }

    public synchronized boolean sendMail(String receiver, String title, String message) {
        try {

            // TODO - replace with mail
            System.err.println("MAIL> " + sender + " -> " + receiver + " : " + title + "\n" + message);

            manager.log("[Mail] :: [sender=" + sender + "],[receiver=" + receiver + "],[title=" + title + "] :: delivered successfully!");
            return true;
        }catch (RuntimeException e){
            manager.log("[Mail] :: [sender=" + sender + "],[receiver=" + receiver + "],[title=" + title + "] :: could not be delivered!");
            return false;
        }
    }

}
