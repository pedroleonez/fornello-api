package pedroleonez.fornello.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pedroleonez.fornello.api.entities.ProductVariation;

import java.util.Optional;

public interface ProductVariationRepository extends JpaRepository<ProductVariation, Long> {

    @Query("select pv from ProductVariation pv where pv.product.id = :productId and pv.id = :productVariationId")
    Optional<ProductVariation> findByProductIdAndProductVariationId(@Param("productId") Long productId, @Param("productVariationId") Long productVariationId);
}
