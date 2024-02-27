package ecommerce.sumbermakmur.service.impl;

import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransSnapApi;
import ecommerce.sumbermakmur.constant.EOrder;
import ecommerce.sumbermakmur.dto.OrderDetails;
import ecommerce.sumbermakmur.dto.OrderRequest;
import ecommerce.sumbermakmur.dto.response.OrderDetailResponse;
import ecommerce.sumbermakmur.dto.response.OrderResponse;
import ecommerce.sumbermakmur.entity.*;
import ecommerce.sumbermakmur.repository.OrderRepository;
import ecommerce.sumbermakmur.service.*;
import ecommerce.sumbermakmur.utils.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;

    private final OrderDetailService orderDetailService;

    private final CustomerService customerService;

    private final CartService cartService;

    private final ProductService productService;

    private final PaymentService paymentService;

    private final ValidationUtils utils;

    private static final String ROLE = "ADMIN";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderResponse create(OrderRequest request) throws MidtransError {
        utils.validate(request);

        Customer byIdCustomer = customerService.getById(request.getCustomerId());

        Order order = Order.builder()
                .dateOrder(new Date())
                .customer(byIdCustomer)
                .status(EOrder.PENDING)
                .build();

        repository.saveAndFlush(order);

        List<OrderDetailResponse> responses = new ArrayList<>();

        for (OrderDetails orderDetails : request.getDetails()){
            Cart cart = cartService.get(orderDetails.getCartId());

            Product product = productService.getById(cart.getProduct().getId());

            Long count = cart.getPrice() * cart.getQuantity();

            OrderDetail orderDetail = OrderDetail.builder()
                    .order(order)
                    .cart(cart)
                    .countPrice(count)
                    .build();
            orderDetailService.create(orderDetail);

            product.setStock(product.getStock() - cart.getQuantity());
            productService.updateProduct(product);

            Payment payment = Payment.builder()
                    .methodPayment("MIDTRANS")
                    .orderDetail(orderDetail)
                    .totalPayment(orderDetail.getCountPrice())
                    .linkPayment(getPayment(orderDetail))
                    .build();

            paymentService.create(payment);

            OrderDetailResponse response = OrderDetailResponse.builder()
                    .orderDetailId(orderDetail.getId())
                    .cartId(cart.getId())
                    .price(cart.getPrice())
                    .quantity(cart.getQuantity())
                    .productId(cart.getProduct().getId())
                    .productName(cart.getProduct().getNameProduct())
                    .countPrice(orderDetail.getCountPrice())
                    .build();

            responses.add(response);
        }

        return OrderResponse.builder()
                .id(order.getId())
                .idCustomer(order.getCustomer().getId())
                .customerName(order.getCustomer().getLastName())
                .dateOrder(order.getDateOrder())
                .status(order.getStatus().name())
                .orderDetailResponses(responses)
                .build();
    }

    @Override
    public OrderResponse get(String id) {

        Order order = repository.findByCustomerId(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order NOT_FOUND")
        );

        List<OrderDetail> orderDetails = orderDetailService.get(order.getId());

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userCredential = order.getCustomer().getUser();

        if (!currentUser.getId().equals(userCredential.getId()) &&
                currentUser.getAuthorities().stream().noneMatch(role -> role.getAuthority().equals(ROLE)))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "FORBIDDEN");

        List<OrderDetailResponse> responses = new ArrayList<>();

        for (OrderDetail orderDetail : orderDetails){
            OrderDetailResponse response = OrderDetailResponse.builder()
                    .orderDetailId(orderDetail.getId())
                    .cartId(order.getId())
                    .price(orderDetail.getCart().getPrice())
                    .quantity(orderDetail.getCart().getQuantity())
                    .productId(orderDetail.getCart().getProduct().getId())
                    .productName(orderDetail.getCart().getProduct().getNameProduct())
                    .countPrice(orderDetail.getCountPrice())
                    .build();

            responses.add(response);
        }

        return OrderResponse.builder()
                .id(order.getId())
                .idCustomer(order.getCustomer().getId())
                .customerName(order.getCustomer().getLastName())
                .dateOrder(order.getDateOrder())
                .status(order.getStatus().name())
                .orderDetailResponses(responses)
                .build();
    }

    private String getPayment(OrderDetail order) throws MidtransError {

        Config snapConfigOptions = Config.builder()
                .setServerKey("YOUR_SERVER_KEY")
                .setClientKey("YOUR_CLIENT_KEY")
                .setIsProduction(false)
                .build();

        MidtransSnapApi snapApi = new ConfigFactory(snapConfigOptions).getSnapApi();

        Map<String, Object> params = new HashMap<>();

        Map<String, Object> transactionDetails = new HashMap<>();
        transactionDetails.put("order_id", order.getOrder().getId());
        transactionDetails.put("price", order.getCountPrice());

        Map<String, Object> customerDetails = new HashMap<>();
        customerDetails.put("first_name", order.getOrder().getCustomer().getFirstName());
        customerDetails.put("email", order.getOrder().getCustomer().getUser().getEmail());

        params.put("transaction_details", transactionDetails);
        params.put("customer_details", customerDetails);

        return snapApi.createTransactionRedirectUrl(params);
    }
}
