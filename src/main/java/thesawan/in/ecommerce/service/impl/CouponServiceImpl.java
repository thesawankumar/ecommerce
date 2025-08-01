package thesawan.in.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import thesawan.in.ecommerce.model.Cart;
import thesawan.in.ecommerce.model.Coupon;
import thesawan.in.ecommerce.model.User;
import thesawan.in.ecommerce.repository.CartRepository;
import thesawan.in.ecommerce.repository.CouponRepository;
import thesawan.in.ecommerce.repository.UserRepository;
import thesawan.in.ecommerce.service.CouponService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Override
    public Cart applyCouponToCart(String code, double orderValue, User user) throws Exception {
        Coupon coupon = couponRepository.findByCode(code);
        Cart cart = cartRepository.findByUserId(user.getId());
        if (coupon == null) {
            throw new Exception("Coupon not found");
        }
        if (user.getUsedCoupons().contains(coupon)) {
            throw new Exception("Coupon already used");
        }
        if (orderValue < coupon.getMinimumOrderValue()) {
            throw new Exception("Order value does not meet the minimum requirement for this coupon");
        }
        if (coupon.isActive() && LocalDate.now().isAfter(coupon.getValidityStartDate()) &&
                LocalDate.now().isBefore(coupon.getValidityEndDate())) {
            user.getUsedCoupons().add(coupon);
            userRepository.save(user);
            double discountAmount = (cart.getTotalSellingPrice() * coupon.getDiscountPercentage()) / 100;
            cart.setTotalSellingPrice(cart.getTotalSellingPrice() - discountAmount);
            cart.setCouponCode(code);
            cartRepository.save(cart);
            return cart;
        }
        throw new Exception("Coupon is not valid or has expired");
    }

    @Override
    public Cart removeCouponFromCart(String code, User user) throws Exception {
        Coupon coupon = couponRepository.findByCode(code);
        if (coupon == null) {
            throw new Exception("Coupon not found");
        }
        Cart cart = cartRepository.findByUserId(user.getId());
        double discountAmount = (cart.getTotalSellingPrice() * coupon.getDiscountPercentage()) / 100;
        cart.setTotalSellingPrice(cart.getTotalSellingPrice() - discountAmount);
        cart.setCouponCode(null);
        return cartRepository.save(cart);
    }

    @Override
    public Coupon findCCouponById(Long id) throws Exception {
        return couponRepository.findById(id)
                .orElseThrow(() -> new Exception("Coupon not found with id: " + id));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public List<Coupon> findAllCoupons() {
        return couponRepository.findAll();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCoupon(Long id) throws Exception {
        findCCouponById(id);
        couponRepository.deleteById(id);
    }
}
