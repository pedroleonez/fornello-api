package pedroleonez.fornello.api.exceptions;

public class ProductVariationNotFoundException extends RuntimeException {
    public ProductVariationNotFoundException() {
        super("Product variation not found.");
    }

}
