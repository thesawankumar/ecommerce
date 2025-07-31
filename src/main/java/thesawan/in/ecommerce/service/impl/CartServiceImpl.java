package thesawan.in.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import thesawan.in.ecommerce.model.Cart;
import thesawan.in.ecommerce.model.CartItem;
import thesawan.in.ecommerce.model.Product;
import thesawan.in.ecommerce.model.User;
import thesawan.in.ecommerce.repository.CartItemRepository;
import thesawan.in.ecommerce.repository.CartRepository;
import thesawan.in.ecommerce.service.CartService;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem addToCart(User user, Product product, String size, int quantity) {

        Cart cart = findUserCart(user);

        CartItem isPresent = cartItemRepository.findByCartIdAndProductIdAndSize(cart.getId(), product.getId(), size);
        if (isPresent == null) {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setSize(size);
            cartItem.setQuantity(quantity);
            cartItem.setUserId(user.getId());
            int totalPrice = product.getSellingPrice() * quantity;
            cartItem.setSellingPrice(totalPrice);
            cartItem.setMrpPrice(quantity * product.getMrpPrice());

            cart.getCartItems().add(cartItem);
            cartItem.setCart(cart);

            return cartItemRepository.save(cartItem);

        }
        return isPresent;
    }

    @Override
    public Cart findUserCart(User user) {
        Cart cart = cartRepository.findByUserId(user.getId());

        int totalPrice = 0;
        int totalDiscountPrice = 0;
        int totalItems = 0;
        for (CartItem cartItem : cart.getCartItems()) {
            totalPrice += cartItem.getMrpPrice();
            totalDiscountPrice += cartItem.getSellingPrice();
            totalItems += cartItem.getQuantity();
        }
        cart.setTotalMrpPrice(totalPrice);
        cart.setTotalItem(totalItems);
        cart.setTotalSellingPrice(totalDiscountPrice);
        cart.setDiscount(calculateDiscountPercentage(totalPrice, totalDiscountPrice));
        return cart;
    }

    private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {
        if (mrpPrice <= 0 || sellingPrice < 0) {
            return 0;
        }
        return (mrpPrice - sellingPrice) * 100 / mrpPrice;
    }

}
