package ecommerce.sumbermakmur.service;

import ecommerce.sumbermakmur.dto.ReviewRequest;
import ecommerce.sumbermakmur.dto.SearchReviewRequest;
import ecommerce.sumbermakmur.dto.response.ReviewResponse;
import org.springframework.data.domain.Page;

public interface ReviewService {

    ReviewResponse create(ReviewRequest request);

    ReviewResponse get(String id);

    void delete(String id);

    Page<ReviewResponse> search(SearchReviewRequest request);
}
