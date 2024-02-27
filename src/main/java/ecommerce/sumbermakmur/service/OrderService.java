package ecommerce.sumbermakmur.service;

import com.midtrans.httpclient.error.MidtransError;
import ecommerce.sumbermakmur.dto.OrderRequest;
import ecommerce.sumbermakmur.dto.response.OrderResponse;

public interface OrderService {

    OrderResponse create(OrderRequest request) throws MidtransError;

    OrderResponse get(String id);
}
