package ecommerce.sumbermakmur.service;

import ecommerce.sumbermakmur.dto.OrderRequest;
import ecommerce.sumbermakmur.dto.response.OrderResponse;

public interface OrderService {

    OrderResponse create(OrderRequest request);
}
