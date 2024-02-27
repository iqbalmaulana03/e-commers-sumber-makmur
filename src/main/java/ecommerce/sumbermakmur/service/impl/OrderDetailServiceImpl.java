package ecommerce.sumbermakmur.service.impl;

import ecommerce.sumbermakmur.entity.OrderDetail;
import ecommerce.sumbermakmur.repository.OrderDetailRepository;
import ecommerce.sumbermakmur.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository repository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(OrderDetail orderDetail) {
        repository.saveAndFlush(orderDetail);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public List<OrderDetail> get(String orderId) {
        return repository.findByOrderId(orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public OrderDetail getById(String id) {
        return repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT_FOUND")
        );
    }
}
