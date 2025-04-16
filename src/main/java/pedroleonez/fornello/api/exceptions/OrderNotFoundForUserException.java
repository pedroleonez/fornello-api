package pedroleonez.fornello.api.exceptions;

public class OrderNotFoundForUserException extends RuntimeException {

    public OrderNotFoundForUserException() {
        super("Order not found for user.");
    }

}
