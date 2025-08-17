package thesawan.in.ecommerce.controller;

import com.razorpay.PaymentLink;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thesawan.in.ecommerce.domain.PaymentMethod;
import thesawan.in.ecommerce.model.*;
import thesawan.in.ecommerce.repository.AddressRepository;
import thesawan.in.ecommerce.repository.PaymentOrderRepository;
import thesawan.in.ecommerce.response.PaymentLinkResponse;
import thesawan.in.ecommerce.service.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final CartService cartService;
    private final SellerService sellerService;
    private final SellerReportService sellerReportService;
    private final PaymentService paymentService;
    private final PaymentOrderRepository paymentOrderRepository;
    private final AddressRepository addressRepository;
//
//    @PostMapping("/create-order")
//    public ResponseEntity<PaymentLinkResponse> createOrderHandler(@RequestBody Address shippingAddress,
//                                                                  @RequestParam PaymentMethod paymentMethod,
//                                                                  @RequestHeader("Authorization") String jwt) throws Exception {
//        // Validate user JWT token
//        User user = userService.findUserByJwtToken(jwt);
//        Cart cart = cartService.findUserCart(user);
//        if (cart == null || cart.getCartItems().isEmpty()) {
//            throw new Exception("Cart is empty. Please add items to the cart before placing an order.");
//        }
//        // Create order and get payment link
//        Set<Order> orders = orderService.createOrder(user, shippingAddress, cart);
//
//        PaymentOrder paymentOrder = paymentService.createPaymentOrder(user, orders);
//
//        PaymentLinkResponse res = new PaymentLinkResponse();
//
//        if (paymentMethod.equals(PaymentMethod.RAZORPAY)) {
//            PaymentLink paymentLink = paymentService.createRazorpayPaymentLink(user, paymentOrder.getAmount(), paymentOrder.getAmount());
//            String paymentUrl = paymentLink.get("short_url");
//            String paymentUrlId = paymentLink.get("id");
//            res.setPayment_link_url(paymentUrl);
//            paymentOrder.setPaymentLinkId(paymentUrlId);
//            paymentOrderRepository.save(paymentOrder);
//        }
//        else {
//            String paymentUrl = paymentService.createStripePaymentLink(user, paymentOrder.getAmount(), paymentOrder.getId());
//            res.setPayment_link_url(paymentUrl);
//
//        }
//        return new ResponseEntity<>(res, HttpStatus.OK);
//
//    }


    @PostMapping("/create-order")
    public ResponseEntity<PaymentLinkResponse> createOrderHandler(
            @RequestBody Map<String, Long> payload, // get addressId
            @RequestParam PaymentMethod paymentMethod,
            @RequestHeader("Authorization") String jwt) throws Exception {

        Long addressId = payload.get("addressId");
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.findUserCart(user);

        if (cart == null || cart.getCartItems().isEmpty()) {
            throw new Exception("Cart is empty. Please add items to the cart before placing an order.");
        }

        // Fetch managed Address entity
        Address shippingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new Exception("Address not found"));

        Set<Order> orders = orderService.createOrder(user, shippingAddress, cart);

        PaymentOrder paymentOrder = paymentService.createPaymentOrder(user, orders);

        PaymentLinkResponse res = new PaymentLinkResponse();

        if (paymentMethod.equals(PaymentMethod.RAZORPAY)) {
            PaymentLink paymentLink = paymentService.createRazorpayPaymentLink(user, paymentOrder.getAmount(), paymentOrder.getAmount());
            String paymentUrl = paymentLink.get("short_url");
            String paymentUrlId = paymentLink.get("id");
            res.setPayment_link_url(paymentUrl);
            paymentOrder.setPaymentLinkId(paymentUrlId);
            paymentOrderRepository.save(paymentOrder);
        }

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/user-orders")
    public ResponseEntity<List<Order>> getOrdersByUserId(@RequestHeader("Authorization") String jwt) throws Exception {
        // Validate user JWT token
        User user = userService.findUserByJwtToken(jwt);
        List<Order> orders = orderService.usersOrdersHistory(user.getId());
        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId,
                                              @RequestHeader("Authorization") String jwt) throws Exception {
        // Validate user JWT token
        User user = userService.findUserByJwtToken(jwt);
        Order order = orderService.findOrderById(orderId);
        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }

    @GetMapping("/order-item/{orderItemId}")
    public ResponseEntity<OrderItem> getOrderItemById(@PathVariable Long orderItemId,
                                                      @RequestHeader("Authorization") String jwt) throws Exception {
        // Validate user JWT token
        User user = userService.findUserByJwtToken(jwt);
        OrderItem orderItem = orderService.findOrderItemById(orderItemId);
        return new ResponseEntity<>(orderItem, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long orderId,
                                             @RequestHeader("Authorization") String jwt) throws Exception {
        // Validate user JWT token
        User user = userService.findUserByJwtToken(jwt);
        Order order = orderService.cancelOrder(orderId, user);
        Seller seller = sellerService.getSellerById(order.getSellerId());
        SellerReport report = sellerReportService.getSellerReport(seller);
        report.setCanceledOrders(report.getCanceledOrders() + 1);
        report.setTotalRefunds(report.getTotalRefunds() + order.getTotalSellingPrice());
        sellerReportService.updateSellerReport(report);
        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }


}
