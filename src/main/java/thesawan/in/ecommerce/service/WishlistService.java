package thesawan.in.ecommerce.service;

import org.springframework.stereotype.Service;
import thesawan.in.ecommerce.model.Product;
import thesawan.in.ecommerce.model.User;
import thesawan.in.ecommerce.model.Wishlist;


public interface WishlistService {
    Wishlist createWishlist(User user);

    Wishlist getWishlistByUserId(User user);

    Wishlist addItemToWishlist(User user, Product product);

}

