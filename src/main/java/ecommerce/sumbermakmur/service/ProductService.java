package ecommerce.sumbermakmur.service;

import ecommerce.sumbermakmur.dto.ProductRequest;
import ecommerce.sumbermakmur.dto.SearchProductRequest;
import ecommerce.sumbermakmur.dto.response.ProductResponse;
import ecommerce.sumbermakmur.entity.Product;

import java.util.List;

public interface ProductService {

    ProductResponse create(ProductRequest request);

    ProductResponse get(String id);

    ProductResponse update(ProductRequest request);

    void delete(String id);

    List<ProductResponse> search(SearchProductRequest request);

    Product getById(String id);

    void updateProduct(Product product);
}
