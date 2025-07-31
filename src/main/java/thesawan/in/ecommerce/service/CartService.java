package thesawan.in.ecommerce.service;

import thesawan.in.ecommerce.model.Cart;
import thesawan.in.ecommerce.model.CartItem;
import thesawan.in.ecommerce.model.Product;
import thesawan.in.ecommerce.model.User;

public interface CartService {
    public CartItem addToCart(User user,
                              Product product,
                              String size, int quantity);

    public Cart findUserCart(User user);

}
