package ecommerce.sumbermakmur.service.impl;

import ecommerce.sumbermakmur.dto.SearchProductRequest;
import ecommerce.sumbermakmur.entity.Product;
import ecommerce.sumbermakmur.entity.ProductCategoryDetail;
import ecommerce.sumbermakmur.repository.ProductCategoryDetailRepository;
import ecommerce.sumbermakmur.service.ProductCategoryDetailService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductCategoryDetailServiceImpl implements ProductCategoryDetailService {

    private final ProductCategoryDetailRepository repository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createOrDetail(ProductCategoryDetail request) {
        repository.save(request);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<ProductCategoryDetail> get(String id) {
        return repository.findByProductId(id);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Page<ProductCategoryDetail> search(SearchProductRequest request) {
        Specification<ProductCategoryDetail> specification = getProductSpecification(request);

        if(request.getPage() <= 0) request.setPage(1);

        Pageable pageable = PageRequest.of((request.getPage() - 1), request.getSize());
        Page<ProductCategoryDetail> all = repository.findAll(specification, pageable);
        List<ProductCategoryDetail> responses = all.getContent();

        return new PageImpl<>(responses, pageable, all.getTotalElements());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        repository.deleteByProductId(id);
    }

    private static Specification<ProductCategoryDetail> getProductSpecification(SearchProductRequest request) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            String product = "product";

            if (Objects.nonNull(request.getProductName())) {
                Join<ProductCategoryDetail, Product> productJoin = root.join(product, JoinType.INNER);
                predicates.add(criteriaBuilder.like(productJoin.get("productName"), "%" + request.getProductName() + "%"));
            }

            if (Objects.nonNull(request.getMinPrice())) {
                Join<ProductCategoryDetail, Product> productJoin = root.join(product, JoinType.INNER);
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(productJoin.get("price"), request.getMinPrice()));
            }

            if (Objects.nonNull(request.getMaxPrice())) {
                Join<ProductCategoryDetail, Product> productJoin = root.join(product, JoinType.INNER);
                predicates.add(criteriaBuilder.lessThanOrEqualTo(productJoin.get("price"), request.getMaxPrice()));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        });
    }
}
