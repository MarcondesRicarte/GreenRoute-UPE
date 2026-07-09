package exceptions;

public class ConectorIncompativelException extends RuntimeException {
    public ConectorIncompativelException(String message) {
        super(message);
    }
}