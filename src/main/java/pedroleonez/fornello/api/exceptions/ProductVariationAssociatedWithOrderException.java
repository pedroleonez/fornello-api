package pedroleonez.fornello.api.exceptions;

public class ProductVariationAssociatedWithOrderException extends RuntimeException{

    public ProductVariationAssociatedWithOrderException() {
        super("Product variation cannot be deleted because it is associated with one or more orders.");
    }
}
