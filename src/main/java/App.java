import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;


public class App {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello world.");

        class FirstHttpHandler implements HttpHandler {
            public void handle(HttpExchange t) throws IOException {
                String response = "<html><body><b>This is a test response</b></body></html>";
                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }

        int port = 8001;
        HttpServer server =  HttpServer.create(new InetSocketAddress(port),0);
        server.createContext("/test", new FirstHttpHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("End1");
    }
}
