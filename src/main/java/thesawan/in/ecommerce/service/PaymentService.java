package thesawan.in.ecommerce.service;


import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import thesawan.in.ecommerce.model.Order;
import thesawan.in.ecommerce.model.PaymentOrder;
import thesawan.in.ecommerce.model.User;

import java.util.Set;

public interface PaymentService {
    PaymentOrder createPaymentOrder(User user, Set<Order> orders);

    PaymentOrder getPaymentOrderById(Long paymentOrderId) throws Exception;

    PaymentOrder getPaymentOrderByPaymentId(String orderId) throws Exception;

    Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) throws RazorpayException;

    PaymentLink createRazorpayPaymentLink(User user, Long amount, Long orderId) throws RazorpayException;

    String createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException;
}
