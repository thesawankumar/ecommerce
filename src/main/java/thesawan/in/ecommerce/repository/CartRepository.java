package thesawan.in.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thesawan.in.ecommerce.model.Cart;

public interface CartRepository extends JpaRepository<Cart,Long> {
}
