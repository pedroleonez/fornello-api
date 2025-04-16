package pedroleonez.fornello.api.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException() {
        super("Product not found.");
    }

}
