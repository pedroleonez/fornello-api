package pedroleonez.fornello.api.exceptions;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException() {
        super("User not found.");
    }

}
