package thesawan.in.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thesawan.in.ecommerce.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
