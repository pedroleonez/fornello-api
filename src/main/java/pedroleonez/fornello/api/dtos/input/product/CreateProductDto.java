package pedroleonez.fornello.api.dtos.input.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateProductDto(

        @NotBlank
        String name,

        @NotBlank
        String description,

        @NotBlank
        String category,

        @NotEmpty
        List<CreateProductVariationDto> productVariations,

        @NotNull
        Boolean available
) {
}
