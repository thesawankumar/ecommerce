package thesawan.in.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thesawan.in.ecommerce.model.Product;
import thesawan.in.ecommerce.model.User;
import thesawan.in.ecommerce.model.Wishlist;
import thesawan.in.ecommerce.service.ProductService;
import thesawan.in.ecommerce.service.UserService;
import thesawan.in.ecommerce.service.WishlistService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping("/get-wishlist")
    public ResponseEntity<Wishlist> getWishlistByUserId(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Wishlist wishlist = wishlistService.getWishlistByUserId(user);
        return ResponseEntity.ok(wishlist);
    }

    @PostMapping("/add-product/{productId}")
    public ResponseEntity<Wishlist> addItemToWishlist(@RequestHeader("Authorization") String jwt, @PathVariable Long productId) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Product product = productService.getProductById(productId);
        Wishlist updatedWishlist = wishlistService.addItemToWishlist(user, product);
        return ResponseEntity.ok(updatedWishlist);
    }
}
