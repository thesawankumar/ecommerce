package thesawan.in.ecommerce.service;

import thesawan.in.ecommerce.domain.OrderStatus;
import thesawan.in.ecommerce.model.*;

import java.util.List;
import java.util.Set;

public interface OrderService {

    Set<Order> createOrder(User user, Address shippingAddress, Cart cart);
    Order findOrderById(Long id) throws Exception;
    List<Order> usersOrdersHistory(Long userId);
    List<Order> sellerOrders(Long sellerId);
    Order updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception;
    Order cancelOrder(Long orderId,User user) throws Exception;
    OrderItem findOrderItemById(Long id) throws Exception;
}
