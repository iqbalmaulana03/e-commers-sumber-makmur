package ecommerce.sumbermakmur.service;

import ecommerce.sumbermakmur.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    void create(OrderDetail orderDetail);

    List<OrderDetail> get(String orderId);

    OrderDetail getById(String id);
}
