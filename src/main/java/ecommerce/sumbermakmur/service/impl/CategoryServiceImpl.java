package ecommerce.sumbermakmur.service.impl;

import ecommerce.sumbermakmur.dto.SearchCategoryRequest;
import ecommerce.sumbermakmur.entity.Category;
import ecommerce.sumbermakmur.repository.CategoryRepository;
import ecommerce.sumbermakmur.service.CategoryService;
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
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    private final ValidationUtils utils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Category create(Category category) {

        utils.validate(category);

        Optional<Category> categoryOptional = repository.findByName(category.getNameCategory());

        if (categoryOptional.isPresent()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "BAD_REQUEST");

        return repository.save(category);
    }

    @Override
    @Transactional(readOnly = true)
    public Category get(String id) {
        return repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT_FOUND")
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Category update(Category category) {

        Category categorySaved = repository.findById(category.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT_FOUND")
        );

        categorySaved.setNameCategory(category.getNameCategory());

        return repository.save(categorySaved);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        Category category = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT FOUND")
        );

        repository.delete(category);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Category> search(SearchCategoryRequest request) {
        Specification<Category> specification = getCategorySpecification(request);

        if (request.getPage() <= 0) request.setPage(1);

        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());

        Page<Category> all = repository.findAll(specification, pageable);
        List<Category> responses = all.getContent();

        return new PageImpl<>(responses, pageable, all.getTotalElements());
    }

    private static Specification<Category> getCategorySpecification(SearchCategoryRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(request.getCategoryName())){
                predicates.add(criteriaBuilder.like(root.get("categoryName"), "%"+ request.getCategoryName()+"%"));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }
}
