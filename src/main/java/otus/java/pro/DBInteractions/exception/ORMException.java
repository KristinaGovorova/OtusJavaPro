package otus.java.pro.DBInteractions.exception;

public class ORMException extends RuntimeException {
    public ORMException(String message) {
        super(message);
    }

    public ORMException(String message, Throwable reason) {
        super(message, reason);
    }
}
