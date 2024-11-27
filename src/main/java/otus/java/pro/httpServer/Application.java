package otus.java.pro.httpServer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.io.InputStream;


public class Application {
    public static void main(String[] args) {
        Properties properties = new Properties();
        try (InputStream input = Application.class.getClassLoader().getResourceAsStream("server.properties")) {
            properties.load(input);
            int port = Integer.parseInt(properties.getProperty("port"));
            int threadPoolSize = Integer.parseInt(properties.getProperty("threadPoolSize"));
            HttpServer server = new HttpServer(port, threadPoolSize);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
