package pedroleonez.fornello.api.exceptions;

public class ProductAssociatedWithOrderException extends RuntimeException{

    public ProductAssociatedWithOrderException() {
        super("Product cannot be deleted because it is associated with one or more orders.");
    }

}
