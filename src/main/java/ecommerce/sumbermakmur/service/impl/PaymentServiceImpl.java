package ecommerce.sumbermakmur.service.impl;

import ecommerce.sumbermakmur.entity.Payment;
import ecommerce.sumbermakmur.repository.PaymentRepository;
import ecommerce.sumbermakmur.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Payment payment) {
        repository.saveAndFlush(payment);
    }

}
