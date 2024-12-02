package otus.java.pro.http_server;

public enum StatusCodes {
    OK(200, "OK"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    UNKNOWN_STATUS(0, "Unknown Status");

    private final int code;
    private final String message;

    StatusCodes(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static String getMessageByCode(int statusCode) {
        for (StatusCodes status : StatusCodes.values()) {
            if (status.getCode() == statusCode) {
                return status.getMessage();
            }
        }
        return UNKNOWN_STATUS.getMessage();
    }
}
