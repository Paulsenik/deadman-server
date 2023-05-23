import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class User {

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

}
