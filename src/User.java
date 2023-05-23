import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class User {
    private static List<User> users = new CopyOnWriteArrayList<>();

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

    public User(String userName, String key, long lastSeenUnix, String lastMessage) {
        if (userName == null || key == null)
            throw new NullPointerException();

        this.userName = userName;
        this.key = key;
        this.lastSeenUnix = lastSeenUnix;
        this.lastMessage = lastMessage;

        deathMessage = "User might not be alive. Last response was: " + lastMessage;
        users.add(this);
    }

    public String getName() {
        return userName;
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

    public String getTestMessage() {
        Date d = new Date(lastSeenUnix);
        return "Dear " + userName + ",\nthis test-mail was sent to you as a test by submitting a correct GET-Request to the service.\n\nLast alive-message (" + d.toString() + "):\n" + lastMessage;
    }

    public Set<String> getUserMails() {
        return userMails;
    }

    public Set<String> getContactMails() {
        return contactMails;
    }

    public static List<User> getUsers() {
        return users;
    }

    /**
     * Only returns the correct user if name and key matches
     *
     * @param userName name of the user
     * @param key      the secret key, that authenticates the user
     * @return the User - null if no user with the matching key was found
     */
    public static User getUser(String userName, String key) {
        for (User u : users) {
            if (userName.equals(u.getName()) && key.equals(u.key)) {
                return u;
            }
        }
        return null;
    }

}
