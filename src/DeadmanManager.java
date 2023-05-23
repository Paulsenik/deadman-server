import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public final class DeadmanManager {

    public static final String SRC_indexFile = "web/index.html";


    public static void main(String[] args) throws IOException {
        DeadmanManager dm = new DeadmanManager(80, "deadman@paulsen.ooo", 1);

        // TODO - REMOVE TESTING
        User u = new User("testuser", "testkey", dm.unix(), "");
        u.addUserMail("test@paulsen.ooo");
        u.addContactMail("it@paulsen.ooo");
        u.setMaxWarnTime(10);
        u.setMaxAliveTime(20);
        u.setDeathMessage("Ich bin dann mal weg");
    }

    private final RequestHandler requestHandler;
    private final MailHandler mailHandler;
    private final Timer timeChecker;
    private static List<User> users = new CopyOnWriteArrayList<>();


    private DeadmanManager(int httpPort, String mailAddress, long checkInterval) throws IOException {
        requestHandler = new RequestHandler(httpPort, this);
        mailHandler = new MailHandler(mailAddress, this);
        timeChecker = new Timer();

        timeChecker.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (User u : users) {
                    checkUser(u);
                }
            }
        }, 1000, 1000 * checkInterval);
    }

    /**
     * Chec
     */
    public void checkUser(User u) {
        // TODO
    }

    public boolean checkAlive(String userName, Map<String, String> parameter) {
        // TODO
        return false;
    }

    public boolean checkTest(String userName, Map<String, String> parameter) {
        // TODO
        return false;
    }

    /**
     * Sends same mails to a number of receivers
     *
     * @return true if some mails could be sent
     */
    public boolean triggerMail(Collection<String> receiver, String title, String message) {
        boolean success = false;
        for (String r : receiver) {
            if (r != null) {
                success = success | mailHandler.sendMail(r, title, message);
            }
        }
        return success;
    }

    public void log(String messsage) {
        // TODO - Replace with logger
        System.out.println("[LOG] :: " + System.currentTimeMillis() + " :: " + messsage);
    }

    /**
     * Program-wide function to recieve the current worldwide time.
     * Can easily be modified in the future to accommodate environment-limitations.
     *
     * @return unix-time-stamp
     */
    public long unix() {
        return System.currentTimeMillis() / 1000;
    }
}