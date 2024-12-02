package otus.java.pro.dbinteractions.exception;

public class ORMException extends RuntimeException {
    public ORMException(String message) {
        super(message);
    }

    public ORMException(String message, Throwable reason) {
        super(message, reason);
    }
}
