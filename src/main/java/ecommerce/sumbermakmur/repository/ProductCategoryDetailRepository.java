package ecommerce.sumbermakmur.repository;

import ecommerce.sumbermakmur.entity.ProductCategoryDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProductCategoryDetailRepository extends JpaRepository<ProductCategoryDetail, String>,
        JpaSpecificationExecutor<ProductCategoryDetail> {

    List<ProductCategoryDetail> findByProductId(String productId);

    void deleteByProductId(String id);
}
