package ecommerce.sumbermakmur.service.impl;

import ecommerce.sumbermakmur.dto.ProductDetailRequest;
import ecommerce.sumbermakmur.dto.ProductRequest;
import ecommerce.sumbermakmur.dto.SearchProductRequest;
import ecommerce.sumbermakmur.dto.response.ProductDetail;
import ecommerce.sumbermakmur.dto.response.ProductResponse;
import ecommerce.sumbermakmur.entity.Category;
import ecommerce.sumbermakmur.entity.Product;
import ecommerce.sumbermakmur.entity.ProductCategoryDetail;
import ecommerce.sumbermakmur.repository.ProductRepository;
import ecommerce.sumbermakmur.service.CategoryService;
import ecommerce.sumbermakmur.service.ProductCategoryDetailService;
import ecommerce.sumbermakmur.service.ProductService;
import ecommerce.sumbermakmur.utils.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    private final ProductCategoryDetailService productCategoryDetailService;

    private final CategoryService categoryService;

    private final ValidationUtils utils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductResponse create(ProductRequest request) {
        utils.validate(request);

        Optional<Product> optionalProduct = repository.findByNameProduct(request.getProductName());

        if (optionalProduct.isPresent()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "BAD_REQUEST");

        Product product = Product.builder()
                .nameProduct(request.getProductName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .build();

        repository.saveAndFlush(product);

        List<ProductDetail> productDetails = new ArrayList<>();

        for (ProductDetailRequest productDetail : request.getProductDetails()){
            Category category = categoryService.get(productDetail.getCategoryId());

            ProductCategoryDetail productCategoryDetail = ProductCategoryDetail.builder()
                    .product(product)
                    .category(category)
                    .build();

            productCategoryDetailService.createOrDetail(productCategoryDetail);

            ProductDetail categoryDetail = ProductDetail.builder()
                    .id(productCategoryDetail.getId())
                    .categoryId(category.getId())
                    .categoryName(category.getNameCategory())
                    .build();
            productDetails.add(categoryDetail);
        }

        return ProductResponse.builder()
                .id(product.getId())
                .productName(product.getNameProduct())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .responses(productDetails)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse get(String id) {
        Product product = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT_FOUND")
        );

        List<ProductCategoryDetail> productCategoryDetails = productCategoryDetailService.get(id);

        List<ProductDetail> productDetails = new ArrayList<>();

        for (ProductCategoryDetail productCategoryDetail : productCategoryDetails){
            ProductDetail productDetail = ProductDetail.builder()
                    .id(productCategoryDetail.getId())
                    .categoryId(productCategoryDetail.getCategory().getId())
                    .categoryName(productCategoryDetail.getCategory().getNameCategory())
                    .build();

            productDetails.add(productDetail);
        }
        return ProductResponse.builder()
                .id(product.getId())
                .productName(product.getNameProduct())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .responses(productDetails)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductResponse update(ProductRequest request) {

        utils.validate(request);

        Optional<Product> optionalProduct = repository.findByNameProduct(request.getProductName());

        if (optionalProduct.isPresent()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "BAD_REQUEST");

        Product product = repository.findById(request.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT_FOUND")
        );

        product.setNameProduct(request.getProductName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());

        repository.save(product);

        List<ProductDetail> productDetails = new ArrayList<>();

        for (ProductDetailRequest productDetail : request.getProductDetails()){
            Category category = categoryService.get(productDetail.getCategoryId());

            ProductCategoryDetail productCategoryDetail = ProductCategoryDetail.builder()
                    .product(product)
                    .category(category)
                    .build();

            productCategoryDetailService.createOrDetail(productCategoryDetail);

            ProductDetail categoryDetail = ProductDetail.builder()
                    .id(productCategoryDetail.getId())
                    .categoryId(category.getId())
                    .categoryName(category.getNameCategory())
                    .build();
            productDetails.add(categoryDetail);
        }

        return ProductResponse.builder()
                .id(product.getId())
                .productName(product.getNameProduct())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .responses(productDetails)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        Product product = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT FOUND")
        );

        productCategoryDetailService.delete(id);
        repository.delete(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> search(SearchProductRequest request) {

        Page<ProductCategoryDetail> search = productCategoryDetailService.search(request);

        List<ProductResponse> responses = new ArrayList<>();

        for (ProductCategoryDetail productCategoryDetail : search.getContent()){

            List<ProductDetail> productDetails = new ArrayList<>();

            List<Category> aLlById = categoryService.getALlById(productCategoryDetail.getCategory().getId());

            for (Category category : aLlById){
                ProductDetail productDetail = ProductDetail.builder()
                        .id(productCategoryDetail.getId())
                        .categoryId(category.getId())
                        .categoryName(category.getNameCategory())
                        .build();

                productDetails.add(productDetail);
            }

            ProductResponse response = ProductResponse.builder()
                    .id(productCategoryDetail.getProduct().getId())
                    .productName(productCategoryDetail.getProduct().getNameProduct())
                    .description(productCategoryDetail.getProduct().getDescription())
                    .price(productCategoryDetail.getProduct().getPrice())
                    .stock(productCategoryDetail.getProduct().getStock())
                    .responses(productDetails)
                    .build();

            responses.add(response);
        }

        return responses;
    }
}
