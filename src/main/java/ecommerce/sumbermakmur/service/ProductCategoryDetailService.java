package ecommerce.sumbermakmur.service;

import ecommerce.sumbermakmur.dto.SearchProductRequest;
import ecommerce.sumbermakmur.entity.ProductCategoryDetail;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductCategoryDetailService {

    void createOrDetail(ProductCategoryDetail request);

    List<ProductCategoryDetail> get(String id);

    Page<ProductCategoryDetail> search(SearchProductRequest request);

    void delete(String id);
}
