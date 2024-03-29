package ooo.paulsen.deadman_server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;

// Others Code
// https://stackoverflow.com/questions/3732109/simple-http-server-in-java-using-only-java-se-api

public final class WebRequestHandler {

    private final HttpServer httpServer;

    public WebRequestHandler(int port) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        httpServer.createContext("/", new ContextHandler(this));
        httpServer.setExecutor(null); // creates a default executor
        httpServer.start();
    }

    public boolean isRunning() {
        return httpServer != null;
    }

    void sendWebHTML(HttpExchange e) throws IOException {
        String line;
        String resp = "";

        // Load index-file into resp
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

}
