package pedroleonez.fornello.api.dtos.output.order;

import pedroleonez.fornello.api.dtos.output.product.RecoveryProductVariationDto;

public record RecoveryOrderItemDto(

        Long id,

        RecoveryProductVariationDto productVariation,

        Integer quantity

) {
}
