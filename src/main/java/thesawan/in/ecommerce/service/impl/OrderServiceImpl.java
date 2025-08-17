package thesawan.in.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import thesawan.in.ecommerce.domain.OrderStatus;
import thesawan.in.ecommerce.domain.PaymentStatus;
import thesawan.in.ecommerce.model.*;
import thesawan.in.ecommerce.repository.AddressRepository;
import thesawan.in.ecommerce.repository.OrderItemRepository;
import thesawan.in.ecommerce.repository.OrderRepository;
import thesawan.in.ecommerce.service.OrderService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final OrderItemRepository orderItemRepository;
//
//    @Override
//    public Set<Order> createOrder(User user, Address shippingAddress, Cart cart) {
//        if (!user.getAddresses().contains(shippingAddress)) {
//            user.getAddresses().add(shippingAddress);
//        }
//        Address address = addressRepository.save(shippingAddress);
//
//        Map<Long, List<CartItem>> itemsBySeller = cart.getCartItems().
//                stream().collect(Collectors.groupingBy(
//                        item -> item.getProduct().getSeller().getId()));
//        Set<Order> orders = new HashSet<>();
//        for (Map.Entry<Long, List<CartItem>> entry : itemsBySeller.entrySet()) {
//            Long sellerId = entry.getKey();
//            List<CartItem> items = entry.getValue();
//            int totalOrderPrice = items.stream().mapToInt(CartItem::getSellingPrice).sum();
//            int totalItem = items.stream().mapToInt(CartItem::getQuantity).sum();
//
//            Order createOrder = new Order();
//            createOrder.setUser(user);
//            createOrder.setSellerId(sellerId);
//            createOrder.setTotalMrpPrice(totalOrderPrice);
//            createOrder.setTotalSellingPrice(totalOrderPrice);
//            createOrder.setTotalItem(totalItem);
//            createOrder.setShippingAddress(address);
//            createOrder.setOrderStatus(OrderStatus.PENDING);
//            createOrder.setPaymentStatus(PaymentStatus.PENDING);
//
//            Order savedOrder = orderRepository.save(createOrder);
//            orders.add(savedOrder);
//
//            List<OrderItem> orderItems = new ArrayList<>();
//            for (CartItem item : items) {
//                OrderItem orderItem = new OrderItem();
//                orderItem.setOrder(savedOrder);
//                orderItem.setProduct(item.getProduct());
//                orderItem.setSize(item.getSize());
//                orderItem.setQuantity(item.getQuantity());
//                orderItem.setSellingPrice(item.getSellingPrice());
//                orderItem.setMrpPrice(item.getMrpPrice());
//                orderItem.setUserId(item.getUserId());
//                savedOrder.getOrderItems().add(orderItem);
//
//                OrderItem savedOrderItem = orderItemRepository.save(orderItem);
//                orderItems.add(savedOrderItem);
//            }
//
//        }
//        return orders;
//    }


    @Override
    public Set<Order> createOrder(User user, Address shippingAddress, Cart cart) {
        Map<Long, List<CartItem>> itemsBySeller = cart.getCartItems()
                .stream()
                .collect(Collectors.groupingBy(item -> item.getProduct().getSeller().getId()));

        Set<Order> orders = new HashSet<>();

        for (Map.Entry<Long, List<CartItem>> entry : itemsBySeller.entrySet()) {
            Long sellerId = entry.getKey();
            List<CartItem> items = entry.getValue();

            int totalOrderPrice = items.stream().mapToInt(CartItem::getSellingPrice).sum();
            int totalItem = items.stream().mapToInt(CartItem::getQuantity).sum();

            Order createOrder = new Order();
            createOrder.setUser(user);
            createOrder.setSellerId(sellerId);
            createOrder.setTotalMrpPrice(totalOrderPrice);
            createOrder.setTotalSellingPrice(totalOrderPrice);
            createOrder.setTotalItem(totalItem);
            createOrder.setShippingAddress(shippingAddress); // use managed entity
            createOrder.setOrderStatus(OrderStatus.PENDING);
            createOrder.setPaymentStatus(PaymentStatus.PENDING);

            Order savedOrder = orderRepository.save(createOrder);

            for (CartItem item : items) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(savedOrder);
                orderItem.setProduct(item.getProduct());
                orderItem.setSize(item.getSize());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setSellingPrice(item.getSellingPrice());
                orderItem.setMrpPrice(item.getMrpPrice());
                orderItem.setUserId(item.getUserId());
                orderItemRepository.save(orderItem);
            }

            orders.add(savedOrder);
        }

        return orders;
    }

    @Override
    public Order findOrderById(Long id) throws Exception {
        return orderRepository.findById(id)
                .orElseThrow(() -> new Exception("Order not found with id: " + id));
    }

    @Override
    public List<Order> usersOrdersHistory(Long userId) {
        return orderRepository.findByUserId(userId);

    }

    @Override
    public List<Order> sellerOrders(Long sellerId) {
        return orderRepository.findBySellerId(sellerId);
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception {
        Order order = findOrderById(orderId);
        order.setOrderStatus(orderStatus);
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Long orderId, User user) throws Exception {
        Order order = findOrderById(orderId);
        if (user.getId().equals(order.getUser().getId())) {
            throw new Exception("You are not authorized to cancel this order");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    @Override
    public OrderItem findOrderItemById(Long id) throws Exception {
        return orderItemRepository.findById(id)
                .orElseThrow(() -> new Exception("Order item not found with id: " + id));
    }

    @Override
    public void deleteOrderBySeller(Long orderId, Long sellerId) throws Exception {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new Exception("Order not found"));

        if (!order.getId().equals(sellerId)) {
            throw new Exception("You are not authorized to delete this order");
        }

        orderRepository.delete(order);
    }

}
