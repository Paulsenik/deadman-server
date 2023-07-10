package ooo.paulsen.deadman_server;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.*;

public final class DeadmanManager {

    public static final String SRC_indexFile = "web/index.html";
    public static DeadmanManager instance;


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

    private final WebRequestHandler requestHandler;
    private final MailHandler mailHandler;
    private final Timer timeChecker;


    public DeadmanManager(int httpPort, String mailAddress, long checkInterval) throws IOException {
        if (instance != null)
            throw new InvalidObjectException("Deadman Can only exist once!");
        instance = this;

        requestHandler = new WebRequestHandler(httpPort);
        mailHandler = new MailHandler(); // creates MailHandler by reading secrets.json
        timeChecker = new Timer();

        timeChecker.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (User u : User.getUsers()) {
                    checkUser(u);
                }
            }
        }, 1000, 1000 * checkInterval);
    }

    /**
     * Checks user, if he has run over the timelimits provided and sends Mails if necessary
     */
    public void checkUser(User u) {

        if (!u.isAlive())
            return;

        // Over WarnTime
        if (u.getLastSeenUnix() + u.getMaxWarnTime() < unix()) {

            if (u.hasSentWarnMessage()) {
                triggerMail(u.getContactMails(),"",u.getWarnMessage());
            }
        }

        // TODO
    }

    /**
     * Updates the time-status on the user
     *
     * @return true if user-key and alive-check was successful
     */
    public boolean updateAlive(String userName, Map<String, String> parameter) {

        // User-Auth
        User u = User.getUser(userName, parameter.get("key"));
        if (u == null) {
            return false;
        }

        String device = parameter.get("device");
        String lastMessage = parameter.get("message");

        u.updateAlive(unix(), device, lastMessage);

        return true;
    }

    /**
     * Sends a test-mail to the given user (does not affect alive-counter!)
     *
     * @return true if user-key and test was successful
     */
    public boolean runTest(String userName, Map<String, String> parameter) {

        // User-Auth
        User u = User.getUser(userName, parameter.get("key"));
        if (u == null) {
            return false;
        }

        return triggerMail(u.getUserMails(), "(Test) Deadman - " + userName, u.getTestMessage());
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
                success = success | mailHandler.queueMail(r, title, message);
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