package ecommerce.sumbermakmur.service.impl;

import ecommerce.sumbermakmur.dto.CartRequest;
import ecommerce.sumbermakmur.dto.UpdateCartRequest;
import ecommerce.sumbermakmur.dto.response.CartResponse;
import ecommerce.sumbermakmur.entity.Cart;
import ecommerce.sumbermakmur.entity.Customer;
import ecommerce.sumbermakmur.entity.Product;
import ecommerce.sumbermakmur.entity.User;
import ecommerce.sumbermakmur.repository.CartRepository;
import ecommerce.sumbermakmur.service.CartService;
import ecommerce.sumbermakmur.service.CustomerService;
import ecommerce.sumbermakmur.service.ProductService;
import ecommerce.sumbermakmur.utils.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository repository;

    private final ValidationUtils utils;

    private final CustomerService customerService;

    private final ProductService productService;

    private static final String FORBIDDEN = "FORBIDDEN";

    private static final String NOT_FOUND = "NOT_FOUND";

    private static final String ROLE = "ROLE_ADMIN";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CartResponse create(CartRequest request) {
        utils.validate(request);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = new User();

        if (authentication != null && authentication.getPrincipal() instanceof User loggedUserId){
            user.setId(loggedUserId.getId());
        }

        Customer customer = customerService.getCustomerByUserId(user.getId());

        Product product = productService.getById(request.getProductId());

        if (product.getStock() - request.getQuantity() < 0){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity exceeds Stock");
        }

        if (request.getQuantity() <= 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity min 1");
        }

        Cart cart = Cart.builder()
                .customer(customer)
                .product(product)
                .price(product.getPrice())
                .quantity(request.getQuantity())
                .build();

        repository.save(cart);

        return toCartResponse(cart);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartResponse> getByCustomer(String customerId) {

        List<Cart> byCustomerId = repository.findByCustomerId(customerId);

        List<CartResponse> responses = new ArrayList<>();

        for (Cart cart : byCustomerId){
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = cart.getCustomer().getUser();

            if (!currentUser.getId().equals(user.getId()) &&
                    currentUser.getAuthorities().stream().noneMatch(part -> part.getAuthority().equals(ROLE))){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, FORBIDDEN);
            }

            CartResponse response = CartResponse.builder()
                    .id(cart.getId())
                    .customerId(cart.getCustomer().getId())
                    .customerName(cart.getCustomer().getLastName())
                    .productId(cart.getProduct().getId())
                    .productName(cart.getProduct().getNameProduct())
                    .price(cart.getPrice())
                    .quantity(cart.getQuantity())
                    .build();

            responses.add(response);
        }



        return responses;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CartResponse update(UpdateCartRequest request) {
        utils.validate(request);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = new User();

        if (authentication != null && authentication.getPrincipal() instanceof User loggedUserId){
            user.setId(loggedUserId.getId());
        }

        Customer customer = customerService.getCustomerByUserId(user.getId());

        Cart cart = repository.findById(request.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND)
        );

        cart.setCustomer(customer);
        cart.setQuantity(request.getQuantity());

        repository.save(cart);

        return toCartResponse(cart);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        Cart cart = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND)
        );

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = cart.getCustomer().getUser();

        if (!currentUser.getId().equals(user.getId()) &&
                currentUser.getAuthorities().stream().noneMatch(part -> part.getAuthority().equals(ROLE))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, FORBIDDEN);
        }

        repository.delete(cart);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Cart get(String id) {
        return repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CART NOT FOUND")
        );
    }

    private CartResponse toCartResponse(Cart cart){
        return CartResponse.builder()
                .id(cart.getId())
                .customerId(cart.getCustomer().getId())
                .customerName(cart.getCustomer().getLastName())
                .productId(cart.getProduct().getId())
                .productName(cart.getProduct().getNameProduct())
                .price(cart.getPrice())
                .quantity(cart.getQuantity())
                .build();
    }
}
