package pedroleonez.fornello.api.dtos;

public record UpdateProductDto(

        String name,

        String description,

        Boolean available
) {
}
