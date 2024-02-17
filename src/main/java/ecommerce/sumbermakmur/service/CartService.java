package ecommerce.sumbermakmur.service;

import ecommerce.sumbermakmur.dto.CartRequest;
import ecommerce.sumbermakmur.dto.UpdateCartRequest;
import ecommerce.sumbermakmur.dto.response.CartResponse;

public interface CartService {

    CartResponse create(CartRequest request);

    CartResponse getByCustomer(String customerId);

    CartResponse update(UpdateCartRequest request);

    void delete(String id);
}
