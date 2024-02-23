package ecommerce.sumbermakmur.service.impl;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;

    private final OrderDetailService orderDetailService;

    private final CustomerService customerService;

    private final CartService cartService;

    private final ProductService productService;

    private final ValidationUtils utils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderResponse create(OrderRequest request) {
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

            OrderDetailResponse response = OrderDetailResponse.builder()
                    .orderDetailId(orderDetail.getId())
                    .cartId(cart.getId())
                    .price(cart.getPrice())
                    .quantity(cart.getQuantity())
                    .productId(cart.getProduct().getId())
                    .productName(cart.getProduct().getNameProduct())
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
}
