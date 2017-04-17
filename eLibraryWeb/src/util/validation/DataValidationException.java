package util.validation;

public class DataValidationException extends Exception {
    public DataValidationException() {
        super();
    }

    public DataValidationException(String message) {
        super(message);
    }
}
