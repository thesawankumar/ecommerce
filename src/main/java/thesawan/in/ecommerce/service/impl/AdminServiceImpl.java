package thesawan.in.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import thesawan.in.ecommerce.domain.USER_ROLE;
import thesawan.in.ecommerce.repository.SellerRepository;
import thesawan.in.ecommerce.repository.UserRepository;
import thesawan.in.ecommerce.repository.CouponRepository;
import thesawan.in.ecommerce.repository.OrderRepository;
import thesawan.in.ecommerce.response.AdminProfileResponse;
import thesawan.in.ecommerce.service.AdminService;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private final OrderRepository orderRepository;

    @Override
    public AdminProfileResponse getAdminDashboardStats() {
        Long totalUsers = userRepository.countByRole(USER_ROLE.ROLE_CUSTOMER);
        Long totalSellers = sellerRepository.countByRole(USER_ROLE.ROLE_SELLER);
        Long totalOrders = orderRepository.count();

        return new AdminProfileResponse(totalUsers, totalSellers, totalOrders);
    }
}
