package ooo.paulsen.deadman_server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class ContextHandler implements HttpHandler {
    private WebRequestHandler handler;

    public ContextHandler(WebRequestHandler handler) {
        super();
        this.handler = handler;
    }

    public static void respond(HttpExchange e, int rCode, String message) throws IOException {
        e.sendResponseHeaders(rCode, message.length());
        OutputStream os = e.getResponseBody();
        os.write(message.getBytes());
        os.close();
    }

    @Override
    public void handle(HttpExchange e) throws IOException {

        URI uri = e.getRequestURI();
        Map<String, String> map = getQueryMap(uri.getQuery());
        String path = uri.getPath();
        System.out.println("Path> " + uri.getPath());

        // Teile Pfad in einzeilne Teile
        String[] pathElements = path.substring(1).split("/");

        if (pathElements.length == 2) {
            if (pathElements[1].equals("alive")) {

                //alive successful
                if (DeadmanManager.instance.updateAlive(pathElements[0], map)) {
                    respond(e, 200, "Alive-Check successful!");
                } else {
                    respond(e, 200, "Alive-Check failed!");
                }
                return;

            } else if (pathElements[1].equals("test")) {

                // test successful
                if (DeadmanManager.instance.runTest(pathElements[0], map)) {
                    respond(e, 200, "Test successful!");
                } else {
                    respond(e, 200, "Test failed!");
                }
                return;
            }
        }

        handler.sendWebHTML(e);
        e.close();
    }

    /**
     * @param query - Only parameters without '?' or '#'
     * @return
     */
    public static Map<String, String> getQueryMap(String query) {
        Map<String, String> map = new HashMap<>();
        if (query == null)
            return map;

        if (query.contains("=")) {

            String[] params = query.split("&");

            for (String param : params) {
                String[] pair = param.split("=");
                if (pair.length == 2) {
                    String name = pair[0];
                    String value = pair[1];
                    map.put(name, value);
                }
            }
            return map;
        }
        return map;
    }
}
