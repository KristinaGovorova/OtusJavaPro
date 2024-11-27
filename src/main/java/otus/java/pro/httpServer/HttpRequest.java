package otus.java.pro.httpServer;

import java.util.Map;

public class HttpRequest {
    private String method;
    private String uri;
    private Map<String, String> headers;
    private Map<String, String> parameters;

    public HttpRequest(String method, String uri, Map<String, String> headers, Map<String, String> parameters) {
        this.method = method;
        this.uri = uri;
        this.headers = headers;
        this.parameters = parameters;
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
}
