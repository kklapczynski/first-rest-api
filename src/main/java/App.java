import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;

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

        class ImageFileHandler implements HttpHandler {

            public void handle(HttpExchange t) throws IOException {
                File file = new File("src/main/resources/balon.jpg");
//                File file = new File("src/main/resources/europe_map.svg");
//                Path path = Paths.get("src/main/resources/Lion-vector.svg");
////              System.out.println("path: " + path.toString());
//                Boolean fileNotExists = Files.notExists(path);
//                System.out.println("File not exists?: " + fileNotExists);
                // CORS : cross-origin-resource-sharing blocks access to endpoint from other domain - here angular app running on localhost:4200
                // settings to allow access
                Headers headers = t.getResponseHeaders();
                headers.add("Access-Control-Allow-Origin", "http://localhost:4200");
                headers.add("Content-Type", "image/jpeg");
//                headers.add("Content-Type", "image/svg+xml");
                t.sendResponseHeaders(200, file.length());
                OutputStream os = t.getResponseBody();
                Files.copy(file.toPath(), os);
                os.close();
            }
        }

        int port = 8001;
        HttpServer server =  HttpServer.create(new InetSocketAddress(port),0);
        server.createContext("/test", new FirstHttpHandler());
        server.createContext("/testImage", new ImageFileHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("End1");
    }
}
