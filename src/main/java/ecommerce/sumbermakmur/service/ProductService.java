package ecommerce.sumbermakmur.service;

import ecommerce.sumbermakmur.dto.ProductRequest;
import ecommerce.sumbermakmur.dto.SearchProductRequest;
import ecommerce.sumbermakmur.dto.response.ProductResponse;
import org.springframework.data.domain.Page;

public interface ProductService {

    ProductResponse create(ProductRequest request);

    ProductResponse get(String id);

    ProductResponse update(ProductRequest request);

    void delete(String id);

    Page<ProductResponse> search(SearchProductRequest request);
}
