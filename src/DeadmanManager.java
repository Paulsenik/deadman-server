import java.io.IOException;
import java.util.*;

public final class DeadmanManager {

    public static final String SRC_indexFile = "web/index.html";


    public static void main(String[] args) throws IOException {
        DeadmanManager dm = new DeadmanManager(80, "deadman@paulsen.ooo", 1);

        // TODO - REMOVE TESTING
        User u = new User("testuser", "testkey", dm);
        u.addUserMail("test@paulsen.ooo");
        u.addContactMail("it@paulsen.ooo");
        u.setMaxWarnTime(10);
        u.setMaxAliveTime(20);
        u.setDeathMessage("Ich bin dann mal weg");
    }

    private RequestHandler requestHandler;
    private MailHandler mailHandler;
    private Timer timeChecker;

    private DeadmanManager(int httpPort, String mailAddress, long checkInterval) throws IOException {
        requestHandler = new RequestHandler(httpPort, this);
        mailHandler = new MailHandler(mailAddress);
        timeChecker = new Timer();

        timeChecker.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                triggerCheck();
            }
        }, 1000, 1000 * checkInterval);
    }

    public void triggerCheck() {
        for (User man : User.getUsers()) {
            if (man.getManager() == this) {
                man.checkTime();
            }
        }
    }

    public boolean triggerAlive(String userName, Map<String, String> parameter) {
        for (User man : User.getUsers()) {
            if (man.getManager() == this && man.getUserName().equals(userName))
                return man.updateAlive(parameter.get("key"), parameter.get("device"), parameter.get("message"), unix());
        }
        return false;
    }

    public boolean triggerTest(String userName, Map<String, String> parameter) {
        for (User man : User.getUsers()) {
            if (man.getManager() == this && man.getUserName().equals(userName))
                return man.checkTest(parameter.get("key"), parameter.get("device"), parameter.get("message"), unix());
        }
        return false;
    }

    public boolean triggerMail(Collection<String> receiver, String title, String message) {
        boolean success = false;
        for (String r : receiver) {
            if (r != null) {
                success = success | mailHandler.sendMail(r, title, message);
            }
        }
        if (!success)
            System.out.println("Mail failed");
        return success;
    }

    public void log(String messsage) {
        // TODO - Replace with logger
        System.out.println("[LOG] :: " + messsage);
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