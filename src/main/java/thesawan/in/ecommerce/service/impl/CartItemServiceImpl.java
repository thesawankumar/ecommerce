package thesawan.in.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import thesawan.in.ecommerce.model.CartItem;
import thesawan.in.ecommerce.model.User;
import thesawan.in.ecommerce.repository.CartItemRepository;
import thesawan.in.ecommerce.service.CartItemService;
import thesawan.in.ecommerce.service.CartService;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;


    @Override
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws Exception {
        CartItem item = findCartItemById(id);
        User cartItemUser = item.getCart().getUser();
        if (cartItemUser.getId().equals(userId)) {
            // Directly set quantity from request
            item.setQuantity(cartItem.getQuantity());

            item.setSellingPrice(item.getQuantity() * item.getProduct().getSellingPrice());
            item.setMrpPrice(item.getQuantity() * item.getProduct().getMrpPrice());
            return cartItemRepository.save(item);
        }
        throw new Exception("You are not authorized to update this cart item");
    }

    @Override
    public void deleteCartItem(Long userId, Long cartItemId) throws Exception {

        CartItem item = findCartItemById(cartItemId);
        User cartItemUser = item.getCart().getUser();
        if (cartItemUser.getId().equals(userId)) {
            cartItemRepository.delete(item);
        } else {
            throw new Exception("You are not authorized to delete this cart item");
        }
    }

    @Override
    public CartItem findCartItemById(Long id) throws Exception {
        return cartItemRepository.findById(id)
                .orElseThrow(() -> new Exception("Cart item not found with id: " + id));
    }
}
