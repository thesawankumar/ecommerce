package thesawan.in.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thesawan.in.ecommerce.model.Cart;
import thesawan.in.ecommerce.model.CartItem;
import thesawan.in.ecommerce.model.Product;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByCartIdAndProductIdAndSize(Long cartId, Long productId, String size);
}
