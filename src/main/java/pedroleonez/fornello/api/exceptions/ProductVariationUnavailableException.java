package pedroleonez.fornello.api.exceptions;

public class ProductVariationUnavailableException extends RuntimeException {
    public ProductVariationUnavailableException() {
        super ("Product variation unavailable because there is also no product available.");
    }

}
