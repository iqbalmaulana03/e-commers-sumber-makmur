package ecommerce.sumbermakmur.service;

import ecommerce.sumbermakmur.dto.SearchCategoryRequest;
import ecommerce.sumbermakmur.entity.Category;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {

    Category create(Category category);

    Category get(String id);

    Category update(Category category);

    void delete(String id);

    Page<Category> search(SearchCategoryRequest request);

    List<Category> getALlById(String id);
}
