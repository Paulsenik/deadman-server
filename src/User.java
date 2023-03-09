import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class User {

    private static List<User> users = new CopyOnWriteArrayList<>();

    public static List<User> getUsers() {
        return new ArrayList<>(users);
    }

    private DeadmanManager manager;
    private boolean alive = true;
    private Set<String> userMails = new HashSet<>();
    private Set<String> contactMails = new HashSet<>();
    private String userName;
    private String key;
    private String deathMessage;
    private long maxAliveTime = 60 * 60 * 48, maxWarnTime = 60 * 60 * 24; // default: 2d-alive, 1d-warn

    // last-seen
    private long lastSeenUnix;
    private String lastMessage;
    private boolean hasSentDeathMessage = false, hasSentWarnMessage = false;

    public User(String userName, String key, DeadmanManager manager) {
        if (userName == null || key == null || manager == null)
            throw new NullPointerException();
        this.userName = userName;
        this.key = key;
        this.manager = manager;
        lastSeenUnix = manager.unix();

        deathMessage = "User might not be alive. Last response was: " + lastMessage;

        users.add(this);
    }

    public boolean updateAlive(String key, String device, String message, long unixTimestamp) {
        if (key.equals(this.key)) {
            lastSeenUnix = unixTimestamp;
            hasSentDeathMessage = false;
            hasSentWarnMessage = false;

            lastMessage = message;

            manager.log("[alive] :: [user=" + userName + "] :: [unix=" + unixTimestamp + "] :: [device=" + device + "] :: [message=" + message + "] :: successful");
            return true;
        }
        return false;
    }

    public boolean checkTest(String key, String device, String message, long unixTimestamp) {
        if (key.equals(this.key)) {
            activateTestEvents();
            manager.log("[test] :: [user=" + userName + "] :: [unix=" + unixTimestamp + "] :: [device=" + device + "] :: [message=" + message + "] :: successful");
            return true;
        }
        return false;
    }

    /**
     * Checks if alive- or warn-time is up
     */
    public void checkTime() {
        if (isAlive()) {
            if (lastSeenUnix + maxWarnTime < manager.unix()) {

                if (!hasSentWarnMessage) {
                    activateWarnEvents();
                }

                if (lastSeenUnix + maxAliveTime < manager.unix()) {

                    if (!hasSentDeathMessage) {
                        activateDeathEvents();
                    }
                }
            }
        }
    }

    /**
     * Sends TEST-Mail to test, if service is running correctly
     */
    public void activateTestEvents() {
        java.util.Date time = new java.util.Date(lastSeenUnix * 1000);
        manager.triggerMail(userMails, "(Test) Deadman - " + userName, "Dear " + userName + ",\nthis test-mail was sent as a test by submitting a correct GET-Request to the service.\n\nLast alive-message (" + time.toString() + "):\n" + lastMessage);
    }

    /**
     * Sends warnMessage to user, if the alive-time is about to expire
     */
    private void activateWarnEvents() {
        hasSentWarnMessage = true;
        java.util.Date time = new java.util.Date(lastSeenUnix * 1000);

        StringBuilder b = new StringBuilder();
        b.append(userName);
        b.append(" did not send an alive-message since:  ");
        b.append(time.toString());
        b.append("\n\nLast alive-message (");
        b.append(time.toString());
        b.append("):\n");
        b.append(lastMessage);
        b.append("\n\nIf you have got this mail and are still alive, send a http-get-request with the correct key to cancel and update your alive-time!");

        manager.triggerMail(userMails, "Deadman - " + userName + " - Warning", b.toString());
    }

    /**
     * Sends User-Defined deathmessage to contacts after alive-time expired
     */
    private void activateDeathEvents() {
        hasSentDeathMessage = true;
        String title = "Deadman - " + userName + " might not be alive";
        manager.triggerMail(userMails, title, deathMessage);
        manager.triggerMail(contactMails, title, deathMessage);
    }

    public String getUserName() {
        return userName;
    }

    public DeadmanManager getManager() {
        return manager;
    }

    public void addUserMail(String mailAddress) {
        userMails.add(mailAddress);
    }

    public void addContactMail(String mailAddress) {
        contactMails.add(mailAddress);
    }

    public boolean isAlive() {
        return alive;
    }

    public void setDeathMessage(String message) {
        deathMessage = message;
    }

    /**
     * @param time - in Seconds
     * @return
     */
    public boolean setMaxAliveTime(long time) {
        if (maxWarnTime > maxAliveTime)
            return false;
        maxAliveTime = time;
        return true;
    }

    /**
     * @param time - in Seconds
     * @return
     */
    public boolean setMaxWarnTime(long time) {
        if (maxWarnTime > maxAliveTime)
            return false;
        maxWarnTime = time;
        return true;
    }

}
