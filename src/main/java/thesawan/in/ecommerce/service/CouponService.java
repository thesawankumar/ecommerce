package thesawan.in.ecommerce.service;

import thesawan.in.ecommerce.model.Cart;
import thesawan.in.ecommerce.model.Coupon;
import thesawan.in.ecommerce.model.User;

import java.util.List;

public interface CouponService {
    Cart applyCouponToCart(String code, double orderValue, User user) throws Exception;

    Cart removeCouponFromCart(String code, User user) throws Exception;

    Coupon findCouponById(Long id) throws Exception;

    Coupon createCoupon(Coupon coupon);

    List<Coupon> findAllCoupons();

    void deleteCoupon(Long id) throws Exception;
}
