package ecommerce.sumbermakmur.controller;

import ecommerce.sumbermakmur.dto.ReviewRequest;
import ecommerce.sumbermakmur.dto.SearchReviewRequest;
import ecommerce.sumbermakmur.dto.response.PagingResponse;
import ecommerce.sumbermakmur.dto.response.ReviewResponse;
import ecommerce.sumbermakmur.dto.response.WebResponse;
import ecommerce.sumbermakmur.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService service;

    @PostMapping("/")
    public ResponseEntity<WebResponse<ReviewResponse>> create(@RequestBody ReviewRequest request){
        ReviewResponse reviewResponse = service.create(request);

        WebResponse<ReviewResponse> response = WebResponse.<ReviewResponse>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("successfully create review")
                .data(reviewResponse)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<WebResponse<ReviewResponse>> get(@PathVariable("reviewId") String id){
        ReviewResponse reviewResponse = service.get(id);

        WebResponse<ReviewResponse> response = WebResponse.<ReviewResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully get review")
                .data(reviewResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<WebResponse<String>> delete(@PathVariable("reviewId") String id){
        service.delete(id);

        WebResponse<String> response = WebResponse.<String>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully delete review")
                .data("OK")
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<WebResponse<List<ReviewResponse>>> search(@RequestParam(name = "rate", required = false) Integer rate,
                                                                    @RequestParam(name = "page", defaultValue = "1") Integer page,
                                                                    @RequestParam(name = "size", defaultValue = "10") Integer size){

        SearchReviewRequest request = SearchReviewRequest.builder()
                .rate(rate)
                .page(page)
                .size(size)
                .build();

        Page<ReviewResponse> search = service.search(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .page(request.getPage())
                .size(size)
                .totalElements(search.getTotalElements())
                .totalPage(search.getTotalPages())
                .build();

        WebResponse<List<ReviewResponse>> response = WebResponse.<List<ReviewResponse>>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully get all review")
                .data(search.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }
}
