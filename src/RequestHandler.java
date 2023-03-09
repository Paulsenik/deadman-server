import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

// Others Code
// https://stackoverflow.com/questions/3732109/simple-http-server-in-java-using-only-java-se-api

public final class RequestHandler {

    private HttpServer httpServer;
    private DeadmanManager manager;

    public RequestHandler(int port, DeadmanManager manager) throws IOException {
        if (manager == null)
            throw new IOException("Manager can't be null!");
        this.manager = manager;

        httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        httpServer.createContext("/", new ContextHandler(this));
        httpServer.setExecutor(null); // creates a default executor
        httpServer.start();
    }

    public boolean isRunning() {
        if (httpServer == null)
            return false;
        return true;
    }

    private void sendWebHTML(HttpExchange e) throws IOException {
        System.out.println("send");
        String line;
        String resp = "";

        try {
            File newFile = new File(DeadmanManager.SRC_indexFile);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(newFile)));

            while ((line = bufferedReader.readLine()) != null) {
                resp += line;
            }
            bufferedReader.close();
        } catch (IOException E) {
            E.printStackTrace();
        }

        e.sendResponseHeaders(200, resp.length());
        OutputStream os = e.getResponseBody();
        os.write(resp.getBytes());
        os.close();
    }

    private static final class ContextHandler implements HttpHandler {

        private RequestHandler handler;

        public ContextHandler(RequestHandler handler) {
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
            Map<String, String> map = ContextHandler.getQueryMap(uri.getQuery());
            String path = uri.getPath();
            System.out.println("Path> " + uri.getPath());

            // Teile Pfad in einzeilne Teile
            String[] pathElements = path.substring(1).split("/");

            if (pathElements.length == 2) {
                if (pathElements[1].equals("alive")) {

                    //alive successful
                    if (handler.manager.triggerAlive(pathElements[0], map)) {
                        respond(e, 200, "Alive-Check successful!");
                    } else {
                        respond(e, 200, "Alive-Check failed!");
                    }
                    return;

                } else if (pathElements[1].equals("test")) {

                    // test successful
                    if (handler.manager.triggerTest(pathElements[0], map)) {
                        respond(e, 200, "Test successful!");
                    } else {
                        respond(e, 200, "Test failed!");
                    }
                    return;
                }
            }

            handler.sendWebHTML(e);

        }

        /**
         * @param query - Only parameters without '?' or '#'
         * @return
         */
        public static Map<String, String> getQueryMap(String query) {
            Map<String, String> map = new HashMap<>();
            if(query == null)
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

    /**
    public static void main(String[] args) throws URISyntaxException {
        URI uri = new URI("http://localhost:8000/");

        System.out.println(uri.getQuery() + "\n");

        Map<String, String> map = ContextHandler.getQueryMap(uri.getQuery());
        for (String k : map.keySet())
            System.out.println(k + " " + map.get(k));
    }
     */

}
