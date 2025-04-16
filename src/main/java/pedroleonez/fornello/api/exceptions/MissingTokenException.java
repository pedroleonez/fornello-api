package pedroleonez.fornello.api.exceptions;

public class MissingTokenException extends RuntimeException {

    public MissingTokenException() {
        super("Missing token.");
    }
}
