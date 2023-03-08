import java.io.IOException;
import java.util.HashSet;
import java.util.Map;

public final class DeadmanManager {

    public static final String SRC_indexFile = "web/index.html";

    private HashSet<Deadman> deadmen = new HashSet<>();

    public static void main(String[] args) throws IOException {
        new DeadmanManager();
    }

    private RequestHandler handler;

    private DeadmanManager() throws IOException {
        handler = new RequestHandler(8000, this);

        deadmen.add(new Deadman("testuser","testkey"));
    }

    public boolean triggerAlive(String userName, Map<String, String> parameter) {
        System.out.println("TRIGGERED: ALIVE");
        for (Deadman man : deadmen) {
            if (man.getUserName().equals(userName))
                return man.checkAlive(parameter.get("key"), parameter.get("device"), parameter.get("message"));
        }

        return false;
    }

    public boolean triggerTest(String userName, Map<String, String> parameter) {
        System.out.println("TRIGGERED: TEST");
        for (Deadman man : deadmen) {
            if (man.getUserName().equals(userName))
                return man.checkTest(parameter.get("key"), parameter.get("device"), parameter.get("message"));
        }

        return false;
    }
}