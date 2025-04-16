package pedroleonez.fornello.api.exceptions;

public class UserAssociatedWithOrderException extends RuntimeException{

    public UserAssociatedWithOrderException() {
        super("User cannot be deleted because it is associated with one or more orders.");
    }

}
