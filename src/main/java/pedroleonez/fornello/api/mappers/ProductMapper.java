package pedroleonez.fornello.api.mappers;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pedroleonez.fornello.api.dtos.input.product.CreateProductVariationDto;
import pedroleonez.fornello.api.dtos.output.product.RecoveryProductDto;
import pedroleonez.fornello.api.dtos.output.product.RecoveryProductVariationDto;
import pedroleonez.fornello.api.entities.Product;
import pedroleonez.fornello.api.entities.ProductVariation;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "productVariations", qualifiedByName = "mapProductVariationToRecoveryProductVariationDto")
    RecoveryProductDto mapProductToRecoveryProductDto(Product product);

    @Named("mapProductVariationToRecoveryProductVariationDto")
    @IterableMapping(qualifiedByName = "mapProductVariationToRecoveryProductVariationDto")
    List<RecoveryProductVariationDto> mapProductVariationToRecoveryProductVariationDto(List<ProductVariation> productVariations);

    @Named("mapProductVariationToRecoveryProductVariationDto")
    RecoveryProductVariationDto mapProductVariationToRecoveryProductVariationDto(ProductVariation productVariation);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    ProductVariation mapCreateProductVariationDtoToProductVariation(CreateProductVariationDto createProductVariationDto);
}
