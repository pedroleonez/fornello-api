package pedroleonez.fornello.api.services;

import org.springframework.stereotype.Service;
import pedroleonez.fornello.api.dtos.*;
import pedroleonez.fornello.api.entities.Product;
import pedroleonez.fornello.api.entities.ProductVariation;
import pedroleonez.fornello.api.enums.Category;
import pedroleonez.fornello.api.mappers.ProductMapper;
import pedroleonez.fornello.api.repositories.ProductRepository;
import pedroleonez.fornello.api.repositories.ProductVariationRepository;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductVariationRepository productVariationRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductVariationRepository productVariationRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productVariationRepository = productVariationRepository;
        this.productMapper = productMapper;
    }

    // create product method
    public RecoveryProductDto createProduct(CreateProductDto createProductDto) {
        /*
        converts the list of ProductVariationDto into a list of ProductVariation,
        using ProductMapper to map each element in the list.
         */
        List<ProductVariation> productVariations =  createProductDto.productVariations().stream()
                .map(productVariationDto -> productMapper.mapCreateProductVariationDtoToProductVariation(productVariationDto))
                .toList();

        // creates a product using the data from the DTO
        Product product = Product.builder()
                .name(createProductDto.name())
                .description(createProductDto.description())
                .category(Category.valueOf(createProductDto.category().toUpperCase()))
                .productVariations(productVariations)
                .available(createProductDto.available())
                .build();

        /*
        if the product has available = false, by default all product variations must also have available set to false, because it wouldn't make sense for the product to be unavailable while its variations are available
         */
        if (!product.isAvailable() && product.getProductVariations().stream().anyMatch(ProductVariation::isAvailable)) {
            throw new RuntimeException("A variação de tamanho não pode estar disponível se o produto estiver indisponível.");
        }

        // links each product variation to the product
        productVariations.forEach(productVariation -> productVariation.setProduct(product));

        // save product
        Product productSaved = productRepository.save(product);

        // returning and mapping the products to the RecoveryProductDto type
        return productMapper.mapProductToRecoveryProductDto(productSaved);
    }

    // create ProductVariation method
    public RecoveryProductDto createProductVariation(Long productId, CreateProductVariationDto createProductVariationDto) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Produto não encontrado."));

        // converts the product variation creation DTO to a ProductVariation entity
        ProductVariation productVariation = productMapper.mapCreateProductVariationDtoToProductVariation(createProductVariationDto);

        // links the product variation to the product and saves the variation in the database
        productVariation.setProduct(product);
        ProductVariation productVariationSaved = productVariationRepository.save(productVariation);

        // add the product variation to the product and saves the product in the database
        product.getProductVariations().add(productVariationSaved);
        productRepository.save(product);

        // returning and mapping the products to the RecoveryProductDto type
        return productMapper.mapProductToRecoveryProductDto(productVariationSaved.getProduct());
    }

    // returning all products method
    public List<RecoveryProductDto> getProducts() {
        // returns all products saved in the database
        List<Product> products = productRepository.findAll();

        // returning and mapping the products to a list of RecoveryProductDto type
        return products.stream().map(product -> productMapper.mapProductToRecoveryProductDto(product)).toList();
    }

    // return product by id method
    public RecoveryProductDto getProductById(Long productId) {
        // searches for a product saved in the database
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Produto não encontrado."));

        // returning and mapping the products to RecoveryProductDto type
        return productMapper.mapProductToRecoveryProductDto(product);
    }

    // updates a product (without updating its variations)
    public RecoveryProductDto updateProductPart(Long productId, UpdateProductDto updateProductDto) {
        // searches for a product saved in the database
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Produto não encontrado."));

        /*
        here we have a sequence of if statements that serves as a form of defensive programming: it only changes the value if some value was passed in the Json
        */
        if (updateProductDto.name() != null) {
            product.setName(updateProductDto.name());
        }
        if (updateProductDto.description() != null) {
            product.setDescription(updateProductDto.description());
        }
        if (updateProductDto.available() != null) {
            product.setAvailable(updateProductDto.available());

            /*
            if the product has available = false, by default all product variations must also have available = false, because it wouldn't make sense for the product to be unavailable while its variations are available
            */
            if (!product.isAvailable()) {
                product.getProductVariations().forEach(productVariation -> productVariation.setAvailable(false));
            }
        }

        // returning and mapping the products to RecoveryProductDto type
        return productMapper.mapProductToRecoveryProductDto(productRepository.save(product));
    }

    // updating a product variation method
    public RecoveryProductDto updateProductVariation(Long productId, Long productVariationId, UpdateProductVariationDto updateProductVariationDto) {
        // checks if the product exists
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Produto não encontrado."));

        /*
        searches for the product variation (by id) in the list of variations of the product that is already saved in the database
        */
        ProductVariation productVariation = product.getProductVariations().stream()
                .filter(productVariationInProduct -> productVariationInProduct.getId().equals(productVariationId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Variação de produto não encontrada."));

        if (updateProductVariationDto.sizeName() != null) {
            productVariation.setSizeName(updateProductVariationDto.sizeName());
        }
        if (updateProductVariationDto.description() != null) {
            productVariation.setDescription(updateProductVariationDto.description());
        }
        if (updateProductVariationDto.available() != null) {
            /*
            if the product has available = false, by default the new added variation must also have available = false, because it wouldn't make sense for the product to be unavailable while the variation of that product is available
            */
            if (updateProductVariationDto.available() && !productVariation.getProduct().isAvailable()) {
                throw new RuntimeException("A variação de tamanho não pode estar disponível se o produto estiver indisponível.");
            }
            productVariation.setAvailable(updateProductVariationDto.available());
        }
        if (updateProductVariationDto.price() != null) {
            productVariation.setPrice(updateProductVariationDto.price());
        }

        // saves a product in the database
        Product productSaved = productRepository.save(product);

        // returning and mapping the products to RecoveryProductDto type
        return productMapper.mapProductToRecoveryProductDto(productSaved);
    }

    // deleting a product by id method
    public void deleteProductId(Long productId) {
        // checks if the product exists
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("Produto não encontrado.");
        }
        // deletes a product from the database
        productRepository.deleteById(productId);
    }

    // deleting a product variation by id method
    public void deleteProductVariationById(Long productId, Long productVariationId) {
        // checks if the product variation exists for the product in question
        ProductVariation productVariation = productVariationRepository
                .findByProductIdAndProductVariationId(productId, productVariationId)
                .orElseThrow(() -> new RuntimeException("Variação de produto não encontrada para o produto em questão."));

        // deletes the product variation from the database
        productVariationRepository.deleteById(productVariation.getId());
    }
}
