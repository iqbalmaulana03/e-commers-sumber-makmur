package ecommerce.sumbermakmur.service.impl;

import ecommerce.sumbermakmur.dto.ProductRequest;
import ecommerce.sumbermakmur.dto.SearchProductRequest;
import ecommerce.sumbermakmur.dto.response.ProductResponse;
import ecommerce.sumbermakmur.entity.Product;
import ecommerce.sumbermakmur.repository.ProductRepository;
import ecommerce.sumbermakmur.service.ProductService;
import ecommerce.sumbermakmur.utils.ValidationUtils;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    private final ValidationUtils utils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductResponse create(ProductRequest request) {
        utils.validate(request);

        Optional<Product> optionalProduct = repository.findByProductName(request.getProductName());

        if (optionalProduct.isPresent()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "BAD_REQUEST");

        Product product = Product.builder()
                .nameProduct(request.getProductName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .build();

        repository.save(product);

        return toProductResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse get(String id) {
        Product product = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT_FOUND")
        );

        return toProductResponse(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductResponse update(ProductRequest request) {

        utils.validate(request);

        Product product = repository.findById(request.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT_FOUND")
        );

        product.setNameProduct(request.getProductName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());

        repository.save(product);

        return toProductResponse(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        Product product = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT FOUND")
        );

        repository.delete(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> search(SearchProductRequest request) {
        Specification<Product> specification = getProductSpecification(request);

        if(request.getPage() <= 0) request.setPage(1);

        Pageable pageable = PageRequest.of((request.getPage() - 1), request.getSize());
        Page<Product> all = repository.findAll(specification, pageable);
        List<ProductResponse> responses = all.getContent().stream()
                .map(this::toProductResponse)
                .toList();

        return new PageImpl<>(responses, pageable, all.getTotalElements());

    }

    private static Specification<Product> getProductSpecification(SearchProductRequest request) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(request.getProductName())){
                predicates.add(criteriaBuilder.like(root.get("productName"), "%"+ request.getProductName()+"%"));
            }

            if (Objects.nonNull(request.getMinPrice())) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), request.getMinPrice()));
            }

            if (Objects.nonNull(request.getMaxPrice())) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), request.getMaxPrice()));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        });
    }


    private ProductResponse toProductResponse(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .productName(product.getNameProduct())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
    }
}
