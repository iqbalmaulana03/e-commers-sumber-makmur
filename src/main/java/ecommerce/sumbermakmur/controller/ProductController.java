package ecommerce.sumbermakmur.controller;

import ecommerce.sumbermakmur.dto.ProductRequest;
import ecommerce.sumbermakmur.dto.SearchProductRequest;
import ecommerce.sumbermakmur.dto.response.PagingResponse;
import ecommerce.sumbermakmur.dto.response.ProductResponse;
import ecommerce.sumbermakmur.dto.response.WebResponse;
import ecommerce.sumbermakmur.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService service;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<WebResponse<ProductResponse>> create(@RequestBody ProductRequest product){

        ProductResponse newProduct = service.create(product);

        WebResponse<ProductResponse> response = WebResponse.<ProductResponse>builder()
                .message("successfully create new product")
                .status(HttpStatus.CREATED.getReasonPhrase())
                .data(newProduct)
                .build();


        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<WebResponse<List<ProductResponse>>> getAll(@RequestParam(value = "nameProduct", required = false) String name,
                                                    @RequestParam(value = "minPrice", required = false) Long minPrice,
                                                    @RequestParam(value = "maxPrice", required = false) Long maxPrice,
                                                    @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                    @RequestParam(value = "size", required = false, defaultValue = "10") Integer size){

        SearchProductRequest request = new SearchProductRequest();
        request.setProductName(name);
        request.setMinPrice(minPrice);
        request.setMaxPrice(maxPrice);
        request.setPage(page);
        request.setSize(size);

        Page<ProductResponse> all = service.search(request);

        PagingResponse paging = PagingResponse.builder()
                .page(request.getPage())
                .size(size)
                .totalPage(all.getTotalPages())
                .totalElements(all.getTotalElements())
                .build();

        WebResponse<List<ProductResponse>> response = WebResponse.<List<ProductResponse>>builder()
                .message("successfully get all Product")
                .status(HttpStatus.OK.getReasonPhrase())
                .paging(paging)
                .data(all.getContent())
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/{productId}")
    public ResponseEntity<WebResponse<ProductResponse>> findById(@PathVariable("productId") String id){

        ProductResponse byId = service.get(id);

        WebResponse<ProductResponse> response = WebResponse.<ProductResponse>builder()
                .message("successfully get by id product")
                .status(HttpStatus.OK.getReasonPhrase())
                .data(byId)
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{productId}")
    public ResponseEntity<WebResponse<ProductResponse>> update(@PathVariable("productId") String id,
                                                               @RequestBody ProductRequest request){

        request.setId(request.getId());

        ProductResponse update = service.update(request);

        WebResponse<ProductResponse> response = WebResponse.<ProductResponse>builder()
                .message("successfully update product")
                .status(HttpStatus.OK.getReasonPhrase())
                .data(update)
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{productId}")
    public ResponseEntity<WebResponse<String>> delete(@PathVariable("productId") String id){
        service.delete(id);

        WebResponse<String> response = WebResponse.<String>builder()
                .message("successfully delete product")
                .status(HttpStatus.OK.getReasonPhrase())
                .data("OK")
                .build();

        return ResponseEntity.ok(response);
    }

}
