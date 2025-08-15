package thesawan.in.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thesawan.in.ecommerce.domain.OrderStatus;
import thesawan.in.ecommerce.model.Order;
import thesawan.in.ecommerce.model.Seller;
import thesawan.in.ecommerce.service.OrderService;
import thesawan.in.ecommerce.service.SellerService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller/order")
public class SellerOrderController {

    private final OrderService orderService;
    private final SellerService sellerService;

    @GetMapping("/all-orders")
    public ResponseEntity<List<Order>> getAllOrdersBySellerId(@RequestHeader("Authorization") String jwt) throws Exception {
        // Validate seller JWT token
        Seller seller = sellerService.getSellerProfile(jwt);
        List<Order> orders = orderService.sellerOrders(seller.getId());
        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }

    @PatchMapping("/{orderId}/update-status/{orderStatus}")
    public ResponseEntity<Order> updateOrderStatus(@RequestHeader("Authorization") String jwt,
                                                   @PathVariable Long orderId,
                                                   @PathVariable OrderStatus orderStatus) throws Exception {
        // Validate seller JWT token
        Seller seller = sellerService.getSellerProfile(jwt);
        Order order = orderService.updateOrderStatus(orderId, orderStatus);
        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{orderId}/delete")
    public ResponseEntity<String> deleteOrder(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long orderId) throws Exception {

        // ✅ Validate seller JWT token
        Seller seller = sellerService.getSellerProfile(jwt);

        // ✅ Delete the order
        orderService.deleteOrderBySeller(orderId, seller.getId());

        return new ResponseEntity<>("Order deleted successfully", HttpStatus.OK);
    }


}
