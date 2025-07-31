package thesawan.in.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thesawan.in.ecommerce.model.Cart;
import thesawan.in.ecommerce.model.CartItem;
import thesawan.in.ecommerce.model.Product;
import thesawan.in.ecommerce.model.User;
import thesawan.in.ecommerce.response.AddItemToCartRequest;
import thesawan.in.ecommerce.response.ApiResponse;
import thesawan.in.ecommerce.service.CartItemService;
import thesawan.in.ecommerce.service.CartService;
import thesawan.in.ecommerce.service.ProductService;
import thesawan.in.ecommerce.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {
    private final CartService cartService;
    private final CartItemService cartItemService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping("/user-cart")
    public ResponseEntity<Cart> findUserCartHandler(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.findUserCart(user);
        return new ResponseEntity<Cart>(cart, HttpStatus.OK);
    }

    @PutMapping("/add-item")
    public ResponseEntity<CartItem> addItemToCartHandler(@RequestHeader("Authorization") String jwt,
                                                         @RequestBody AddItemToCartRequest req) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Product product = productService.getProductById(req.getProductId());
        CartItem cartItem = cartService.addToCart(user, product, req.getSize(), req.getQuantity());
        ApiResponse res = new ApiResponse();
        res.setMessage("Item added to cart successfully");
        return new ResponseEntity<>(cartItem, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete-item/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItemHandler(@RequestHeader("Authorization") String jwt,
                                                             @PathVariable Long cartItemId) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        cartItemService.deleteCartItem(user.getId(), cartItemId);
        ApiResponse res = new ApiResponse();
        res.setMessage("Cart item deleted successfully");
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @PutMapping("/update-item/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItemHandler(@RequestHeader("Authorization") String jwt,
                                                          @PathVariable Long cartItemId,
                                                          @RequestBody CartItem cartItem) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        CartItem updatedCartItem = null;
        if (cartItem.getQuantity() <= 0) {
            throw new Exception("Quantity must be greater than zero");
        }
        updatedCartItem = cartItemService.updateCartItem(user.getId(), cartItemId, cartItem);
        return new ResponseEntity<>(updatedCartItem, HttpStatus.ACCEPTED);
    }
}
