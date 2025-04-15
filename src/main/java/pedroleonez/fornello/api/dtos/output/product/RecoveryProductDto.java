package pedroleonez.fornello.api.dtos.output.product;

import java.util.List;

public record RecoveryProductDto(

        Long id,

        String name,

        String description,

        String category,

        List<RecoveryProductVariationDto> productVariations,

        Boolean available
) {
}
