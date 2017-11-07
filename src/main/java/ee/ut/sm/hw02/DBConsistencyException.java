package ee.ut.sm.hw02;

public class DBConsistencyException extends Exception {

    public DBConsistencyException() {
        super();
    }

    public DBConsistencyException(String message) {
        super(message);
    }

    public DBConsistencyException(String message, Throwable cause) {
        super(message, cause);
    }

    public DBConsistencyException(Throwable cause) {
        super(cause);
    }

    protected DBConsistencyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
