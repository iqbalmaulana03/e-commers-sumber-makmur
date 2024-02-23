package ecommerce.sumbermakmur.service.impl;

import ecommerce.sumbermakmur.entity.OrderDetail;
import ecommerce.sumbermakmur.repository.OrderDetailRepository;
import ecommerce.sumbermakmur.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository repository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(OrderDetail orderDetail) {
        repository.saveAndFlush(orderDetail);
    }
}
