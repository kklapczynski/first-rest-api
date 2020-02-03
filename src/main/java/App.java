import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.util.Set;

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

        class SvgHandler implements HttpHandler {
//            TODO: add query = file name to get requested map
            public void handle(HttpExchange t) throws IOException {
                File file = new File("src/main/resources/world.svg");
                // CORS : cross-origin-resource-sharing blocks access to endpoint from other domain - here angular app running on localhost:4200
                // settings to allow access
                Headers headers = t.getResponseHeaders();
                headers.add("Access-Control-Allow-Origin", "http://localhost:4200");
                // set type of data
                headers.add("Content-Type", "image/svg+xml");
                t.sendResponseHeaders(200, file.length());
                OutputStream os = t.getResponseBody();
                Files.copy(file.toPath(), os);
                os.close();
            }
        }

        class SvgListHandler implements HttpHandler {
            public void handle(HttpExchange httpExchange) throws IOException {
                SvgResources svgRes = new SvgResources();
                Headers headers = httpExchange.getResponseHeaders();
                headers.add("Access-Control-Allow-Origin", "http://localhost:4200");
                headers.add("Content-Type", "application/json");
                String filesListJsonString = svgRes.getFilesNamesJson("src/main/resources");
                httpExchange.sendResponseHeaders(200, filesListJsonString.length());
                OutputStream os = httpExchange.getResponseBody();
                os.write(filesListJsonString.getBytes());
                os.close();
            }
        }

        int port = 8001;
        HttpServer server =  HttpServer.create(new InetSocketAddress(port),0);
        server.createContext("/test", new FirstHttpHandler());
        server.createContext("/testImage", new SvgHandler());
        server.createContext("/testMapsList", new SvgListHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        SvgResources svgResources = new SvgResources();
        System.out.println(svgResources.getFilesNamesJson("src/main/resources"));
        System.out.println(svgResources.getFilesNamesArray("src/main/resources"));
        System.out.println("End1");
    }
}
