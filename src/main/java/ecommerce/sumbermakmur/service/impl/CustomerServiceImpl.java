package ecommerce.sumbermakmur.service.impl;

import ecommerce.sumbermakmur.entity.Customer;
import ecommerce.sumbermakmur.repository.CustomerRepository;
import ecommerce.sumbermakmur.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Customer customer) {
        repository.save(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public Customer getCustomerByUserId(String userId) {
        return repository.findByUserId(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT_FOUND")
        );
    }
}
