package ecommerce.sumbermakmur.service.impl;

import ecommerce.sumbermakmur.dto.ReviewRequest;
import ecommerce.sumbermakmur.dto.SearchReviewRequest;
import ecommerce.sumbermakmur.dto.response.ReviewResponse;
import ecommerce.sumbermakmur.entity.Customer;
import ecommerce.sumbermakmur.entity.Product;
import ecommerce.sumbermakmur.entity.Review;
import ecommerce.sumbermakmur.entity.User;
import ecommerce.sumbermakmur.repository.ReviewRepository;
import ecommerce.sumbermakmur.service.CustomerService;
import ecommerce.sumbermakmur.service.ProductService;
import ecommerce.sumbermakmur.service.ReviewService;
import ecommerce.sumbermakmur.utils.ValidationUtils;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository repository;

    private final CustomerService customerService;

    private final ProductService productService;

    private final ValidationUtils utils;

    @Override
    public ReviewResponse create(ReviewRequest request) {

        utils.validate(request);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = new User();

        if (authentication != null && authentication.getPrincipal() instanceof User loggedUserId){
            user.setId(loggedUserId.getId());
        }

        Customer customer = customerService.getCustomerByUserId(user.getId());

        Product product = productService.getById(request.getProductId());

        Review review = Review.builder()
                .customer(customer)
                .product(product)
                .rate(request.getRate())
                .comment(request.getComment())
                .dateReview(new Date())
                .build();

        repository.save(review);

        return toReviewResponse(review);
    }

    @Override
    public ReviewResponse get(String id) {
        Review review = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT_FOUND")
        );

        return toReviewResponse(review);
    }

    @Override
    public void delete(String id) {
        Review review = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT_FOUND")
        );

        repository.delete(review);
    }

    @Override
    public Page<ReviewResponse> search(SearchReviewRequest request) {

        Specification<Review> specification = getReviewSpecification(request);

        if (request.getPage() <= 0) request.setPage(1);

        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());
        Page<Review> all = repository.findAll(specification, pageable);

        List<ReviewResponse> responses = all.getContent().stream()
                .map(this::toReviewResponse)
                .toList();

        return new PageImpl<>(responses, pageable, all.getTotalElements());
    }

    private static Specification<Review> getReviewSpecification(SearchReviewRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(request.getRate())){
                predicates.add(criteriaBuilder.like(root.get("rate"), "%"+ request.getRate()+"%"));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }

    private ReviewResponse toReviewResponse(Review review){
        return ReviewResponse.builder()
                .id(review.getId())
                .customerId(review.getCustomer().getId())
                .customerName(review.getCustomer().getLastName())
                .productId(review.getProduct().getId())
                .productName(review.getProduct().getNameProduct())
                .rate(review.getRate())
                .comment(review.getComment())
                .dateReview(review.getDateReview())
                .build();
    }
}
