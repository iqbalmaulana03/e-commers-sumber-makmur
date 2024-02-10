package ecommerce.sumbermakmur.service;

import ecommerce.sumbermakmur.dto.SearchCategoryRequest;
import ecommerce.sumbermakmur.entity.Category;
import org.springframework.data.domain.Page;

public interface CategoryService {

    Category create(Category category);

    Category get(String id);

    Category update(Category category);

    void delete(String id);

    Page<Category> search(SearchCategoryRequest request);
}
