package ecommerce.sumbermakmur.service;

import ecommerce.sumbermakmur.dto.CustomerRequest;
import ecommerce.sumbermakmur.dto.SearchCustomerRequest;
import ecommerce.sumbermakmur.dto.response.CustomerResponse;
import ecommerce.sumbermakmur.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CustomerService {

    void create(Customer customer);

    Customer getCustomerByUserId(String userId);

    CustomerResponse update(CustomerRequest request);

    CustomerResponse get(String id);

    void delete(String id);

    Page<CustomerResponse> search(SearchCustomerRequest request);

    CustomerResponse uploadAvatar(MultipartFile avatarFileName, String id) throws IOException;
}
