package ecommerce.sumbermakmur.service;

import ecommerce.sumbermakmur.dto.CartRequest;
import ecommerce.sumbermakmur.dto.UpdateCartRequest;
import ecommerce.sumbermakmur.dto.response.CartResponse;
import ecommerce.sumbermakmur.entity.Cart;

import java.util.List;

public interface CartService {

    CartResponse create(CartRequest request);

    List<CartResponse> getByCustomer(String customerId);

    CartResponse update(UpdateCartRequest request);

    void delete(String id);

    Cart get(String id);
}
