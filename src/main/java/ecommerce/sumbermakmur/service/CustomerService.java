package ecommerce.sumbermakmur.service;

import ecommerce.sumbermakmur.entity.Customer;

public interface CustomerService {

    void create(Customer customer);

    Customer getCustomerByUserId(String userId);
}
