import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Deadman {

    private String userName;
    private String key;
    /**
     * The user's Mail-Address for testing and warning
     */
    private Set<String> userMails = new HashSet<>();
    /**
     * The Mails the user set to contact after warn- or alive-time expired
     */
    private Set<String> contactMails = new HashSet<>();

    public Deadman(String userName, String key) {
        this.userName = userName;
        this.key = key;
    }

    public boolean checkAlive(String key, String device, String message) {

        // TODO

        if (key.equals(this.key))
            return true;
        return false;
    }

    public boolean checkTest(String key, String device, String message) {

        // TODO

        if (key.equals(this.key))
            return true;
        return false;
    }

    /**
     * Sends TEST-Mail to test, if service is running correctly
     */
    public void activateTestEvents() {
        System.out.println("TODO - Test-Event for " + userName);
    }

    /**
     * Sends warnMessage to user, if the alive-time is about to expire
     */
    private void avtivateWarnEvents() {
        System.out.println("TODO - Warn-Event for " + userName);
    }

    /**
     * Sends User-Defined deathmessage to contacts after alive-time expired
     */
    private void activateDeathEvents() {
        System.out.println("TODO - Death-Event for " + userName);
    }


    public String getUserName() {
        return userName;
    }
}
