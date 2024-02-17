package ecommerce.sumbermakmur.service.impl;

import ecommerce.sumbermakmur.dto.CustomerRequest;
import ecommerce.sumbermakmur.dto.SearchCustomerRequest;
import ecommerce.sumbermakmur.dto.response.CustomerResponse;
import ecommerce.sumbermakmur.entity.Customer;
import ecommerce.sumbermakmur.entity.User;
import ecommerce.sumbermakmur.repository.CustomerRepository;
import ecommerce.sumbermakmur.service.CustomerService;
import ecommerce.sumbermakmur.service.UserService;
import ecommerce.sumbermakmur.utils.ValidationUtils;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;

    private final UserService userService;

    private final ValidationUtils utils;

    private static final String ROLE = "ROLE_ADMIN";

    private final List<String> contentTypes = List.of("image/jpeg", "image/png");

    private final Path directoryPath;

    private static final String FORBIDDEN = "FORBIDDEN";

    private static final String NOT_FOUND = "NOT_FOUND";

    public CustomerServiceImpl(CustomerRepository repository, ValidationUtils utils, @Value("${app.sumbermakmur.directory-image-path}") String directoryPath, UserService userService) {
        this.repository = repository;
        this.utils = utils;
        this.directoryPath = Paths.get(directoryPath);
        this.userService = userService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Customer customer) {
        repository.save(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public Customer getCustomerByUserId(String userId) {
        return repository.findByUserId(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomerResponse update(CustomerRequest request) {

        utils.validate(request);

        Customer customer = repository.findById(request.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND)
        );

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = customer.getUser();

        if (!currentUser.getId().equals(user.getId()) &&
        currentUser.getAuthorities().stream().noneMatch(part -> part.getAuthority().equals(ROLE))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, FORBIDDEN);
        }

        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setAddress(request.getAddress());
        customer.setPhoneNumber(request.getPhoneNumber());

        repository.save(customer);

        return toCustomerResponse(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse get(String id) {

        Customer customer = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND)
        );

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = customer.getUser();

        if (!currentUser.getId().equals(user.getId()) &&
                currentUser.getAuthorities().stream().noneMatch(part -> part.getAuthority().equals(ROLE))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, FORBIDDEN);
        }

        return toCustomerResponse(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        Customer customer = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND)
        );

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = customer.getUser();

        if (!currentUser.getId().equals(user.getId()) &&
                currentUser.getAuthorities().stream().noneMatch(part -> part.getAuthority().equals(ROLE))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, FORBIDDEN);
        }

        userService.delete(customer.getUser().getId());
        repository.delete(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponse> search(SearchCustomerRequest request) {
        Specification<Customer> specification = getCustomerSpecification(request);

        if (request.getPage() <= 0) request.setPage(1);

        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());
        Page<Customer> customers = repository.findAll(specification, pageable);

        List<CustomerResponse> responses = new ArrayList<>();

        for (Customer customer : customers){
            CustomerResponse response = toCustomerResponse(customer);
            responses.add(response);
        }
        return new PageImpl<>(responses, pageable, customers.getTotalElements());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomerResponse uploadAvatar(MultipartFile avatarFileName, String id) throws IOException {

        saveValidation(avatarFileName);

        Customer customer = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found")
        );

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = customer.getUser();

        if (!currentUser.getId().equals(user.getId()) &&
                currentUser.getAuthorities().stream().noneMatch(part -> part.getAuthority().equals(ROLE))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, FORBIDDEN);
        }

        String fileName = String.format("%d_%s", System.currentTimeMillis(), avatarFileName.getOriginalFilename());

        Path filePath = directoryPath.resolve(fileName);

        Files.copy(avatarFileName.getInputStream(), filePath);

        customer.setAvatarFileName(fileName);
        return toCustomerResponse(customer);
    }

    private static Specification<Customer> getCustomerSpecification(SearchCustomerRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(request.getFirstName())){
                predicates.add(criteriaBuilder.like(root.get("firstName"), "%"+ request.getFirstName()+"%"));
            }

            if (Objects.nonNull(request.getLastName())){
                predicates.add(criteriaBuilder.like(root.get("lastName"), "%"+ request.getLastName()+"%"));
            }

            if (Objects.nonNull(request.getPhoneNumber())){
                predicates.add(criteriaBuilder.like(root.get("phoneNumber"), "%"+ request.getPhoneNumber()+"%"));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }

    private void saveValidation(MultipartFile multipartFile) throws IOException{
        if (multipartFile.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "image is required");
        }
        if (!contentTypes.contains(multipartFile.getContentType())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid content type");
        }
        if (!Files.exists(directoryPath)){
            Files.createDirectories(directoryPath);
        }
    }

    private CustomerResponse toCustomerResponse(Customer customer){
        return CustomerResponse.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .address(customer.getAddress())
                .phoneNumber(customer.getPhoneNumber())
                .avatarFileName(customer.getAvatarFileName())
                .build();
    }
}
