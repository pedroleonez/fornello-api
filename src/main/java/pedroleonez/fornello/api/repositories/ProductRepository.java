package pedroleonez.fornello.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pedroleonez.fornello.api.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
