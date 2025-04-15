package pedroleonez.fornello.api.dtos.input.product;

public record UpdateProductDto(

        String name,

        String description,

        Boolean available
) {
}
