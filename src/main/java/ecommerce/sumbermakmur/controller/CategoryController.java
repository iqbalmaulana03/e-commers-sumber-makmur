package ecommerce.sumbermakmur.controller;

import ecommerce.sumbermakmur.dto.SearchCategoryRequest;
import ecommerce.sumbermakmur.dto.response.PagingResponse;
import ecommerce.sumbermakmur.dto.response.WebResponse;
import ecommerce.sumbermakmur.entity.Category;
import ecommerce.sumbermakmur.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @PostMapping("/")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<WebResponse<Category>> create(@RequestBody Category category){
        Category savedCategory = service.create(category);

        WebResponse<Category> response = WebResponse.<Category>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("successfully create category")
                .data(savedCategory)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{categoryId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<WebResponse<Category>> get(@PathVariable("categoryId") String id){
        Category category = service.get(id);

        WebResponse<Category> response = WebResponse.<Category>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully get by id category")
                .data(category)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{categoryId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<WebResponse<Category>> update(@PathVariable("categoryId") String id,
                                                        @RequestBody Category category){
        category.setId(id);

        Category update = service.update(category);

        WebResponse<Category> response = WebResponse.<Category>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully update category")
                .data(update)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<WebResponse<String>> delete(@PathVariable("categoryId") String id){
        service.delete(id);

        WebResponse<String> response = WebResponse.<String>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully delete category")
                .data("OK")
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<WebResponse<List<Category>>> search(@RequestParam(name = "categoryName", required = false)String categoryName,
                                                              @RequestParam(name = "page", defaultValue = "1")Integer page,
                                                              @RequestParam(name = "size", defaultValue = "10")Integer size){
        SearchCategoryRequest request = SearchCategoryRequest.builder()
                .categoryName(categoryName)
                .page(page)
                .size(size)
                .build();

        Page<Category> search = service.search(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .page(request.getPage())
                .size(size)
                .totalPage(search.getTotalPages())
                .totalElements(search.getTotalElements())
                .build();

        WebResponse<List<Category>> response = WebResponse.<List<Category>>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully get category")
                .data(search.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }
}
