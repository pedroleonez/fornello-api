package pedroleonez.fornello.api.dtos.input.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateProductVariationDto(

        @NotBlank
        String sizeName,

        @NotBlank
        String description,

        @NotNull
        BigDecimal price,

        @NotNull
        Boolean available
) {
}
