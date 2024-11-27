package otus.java.pro.httpServer;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler implements Runnable {
    private Socket clientSocket;
    private HttpServer server;

    public RequestHandler(Socket clientSocket, HttpServer server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String requestLine = in.readLine();
            if (requestLine == null) {
                sendErrorResponse(out, 500);
                return;
            }
            String[] requestParts = requestLine.split(" ");
            if (requestParts.length < 2) {
                sendErrorResponse(out, 500);
                return;
            }
            String method = requestParts[0];
            String uri = requestParts[1];
            Map<String, String> headers = new HashMap<>();
            String line;
            while (!(line = in.readLine()).isEmpty()) {
                String[] headerParts = line.split(": ", 2);
                if (headerParts.length == 2) {
                    headers.put(headerParts[0], headerParts[1]);
                }
            }
            if ("GET".equals(method) && "/shutdown".equals(uri)) {
                server.stop();
                sendResponse(out, 200, "Server shutting down");
                return;
            }
            sendResponse(out, 200, "OK");
        } catch (IOException e) {
            e.printStackTrace();
            try {
                sendErrorResponse(new PrintWriter(clientSocket.getOutputStream(), true), 500);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendResponse(PrintWriter out, int statusCode, String message) {
        out.println("HTTP/1.1 " + statusCode + " " + getStatusMessage(statusCode));
        out.println("Content-Type: text/plain");
        out.println("Content-Length: " + message.length());
        out.println();
        out.println(message);
    }

    private void sendErrorResponse(PrintWriter out, int statusCode) {
        String message = getStatusMessage(statusCode);
        sendResponse(out, statusCode, message);
    }

    private String getStatusMessage(int statusCode) {
        switch (statusCode) {
            case 200:
                return "OK";
            case 500:
                return "Internal Server Error";
            default:
                return "Unknown Status";
        }
    }
}