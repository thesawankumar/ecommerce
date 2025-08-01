package thesawan.in.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thesawan.in.ecommerce.model.Cart;
import thesawan.in.ecommerce.model.Coupon;
import thesawan.in.ecommerce.model.User;
import thesawan.in.ecommerce.service.CartService;
import thesawan.in.ecommerce.service.CouponService;
import thesawan.in.ecommerce.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/coupon")
public class AdminCouponController {
    private final CouponService couponService;
    private final UserService userService;
    private final CartService cartService;


    @PostMapping("/apply-coupon")
    public ResponseEntity<Cart> applyCouponToCart(@RequestParam String code,
                                                  @RequestParam double orderValue,
                                                  @RequestParam String apply,
                                                  @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart;
        if (apply.equals("true")) {
            cart = couponService.applyCouponToCart(code, orderValue, user);
        } else {
            cart = couponService.removeCouponFromCart(code, user);
        }
        return ResponseEntity.ok(cart);
    }

    //admin operations
    @PostMapping("/create")
    public ResponseEntity<Coupon> createCoupon(@RequestBody Coupon coupon) {
        Coupon createdCoupon = couponService.createCoupon(coupon);
        return ResponseEntity.ok(createdCoupon);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCoupon(@PathVariable Long id) throws Exception {
        couponService.deleteCoupon(id);
        return ResponseEntity.ok("Coupon deleted successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<List<Coupon>> getAllCoupons() {
        List<Coupon> coupons = couponService.findAllCoupons();
        return ResponseEntity.ok(coupons);
    }
}
