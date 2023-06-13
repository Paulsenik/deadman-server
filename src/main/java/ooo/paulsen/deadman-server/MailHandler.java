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

}
