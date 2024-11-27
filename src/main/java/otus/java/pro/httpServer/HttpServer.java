package otus.java.pro.httpServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    private int port;
    private int threadPoolSize;
    private boolean running;
    private ExecutorService executorService;

    public HttpServer(int port, int threadPoolSize) {
        this.port = port;
        this.threadPoolSize = threadPoolSize;
        this.running = true;
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);
            while (running) {
                Socket clientSocket = serverSocket.accept();
                executorService.submit(new RequestHandler(clientSocket, this));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        running = false;
        executorService.shutdown();
        System.out.println("Server stopped.");
    }
}
