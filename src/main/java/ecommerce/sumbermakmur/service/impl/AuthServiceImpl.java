package ecommerce.sumbermakmur.service.impl;

import ecommerce.sumbermakmur.constant.ERole;
import ecommerce.sumbermakmur.dto.AuthRequest;
import ecommerce.sumbermakmur.dto.response.LoginResponse;
import ecommerce.sumbermakmur.dto.response.RegisterUserResponse;
import ecommerce.sumbermakmur.entity.Customer;
import ecommerce.sumbermakmur.entity.Role;
import ecommerce.sumbermakmur.entity.User;
import ecommerce.sumbermakmur.repository.UserRepository;
import ecommerce.sumbermakmur.security.JwtUtils;
import ecommerce.sumbermakmur.service.AuthService;
import ecommerce.sumbermakmur.service.CustomerService;
import ecommerce.sumbermakmur.service.RoleService;
import ecommerce.sumbermakmur.utils.ValidationUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final ValidationUtils validationUtils;
    private final RoleService roleService;
    private final CustomerService customerService;

    @PostConstruct
    @Transactional(rollbackFor = Exception.class)
    public void initSuperAdmin(){

        String emailSuper = "superadmin@gmail.com";

        Optional<User> optionalUser = repository.findByEmail(emailSuper);

        if (optionalUser.isPresent()) return;

        Role roleSuperAdmin = roleService.getOrSave(ERole.ROLE_SUPER_ADMIN);

        Role roleAdmin = roleService.getOrSave(ERole.ROLE_ADMIN);

        String encode = passwordEncoder.encode("P@ssword9898");

        User user = User.builder()
                .email(emailSuper)
                .password(encode)
                .parts(List.of(roleSuperAdmin, roleAdmin))
                .build();
        repository.save(user);

        Customer customer = Customer.builder()
                .user(user)
                .build();

        customerService.create(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RegisterUserResponse createCustomer(AuthRequest request) {

        validationUtils.validate(request);

        Role roleCustomer = roleService.getOrSave(ERole.ROLE_CUSTOMER);

        String encode = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .email(request.getEmail())
                .password(encode)
                .parts(List.of(roleCustomer))
                .build();

        repository.saveAndFlush(user);

        Customer customer = Customer.builder()
                .user(user)
                .build();

        customerService.create(customer);

        return toRegisterResponse(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginResponse login(AuthRequest request) {

        validationUtils.validate(request);

        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        );

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();

        String token = jwtUtils.generateToken(user);
        List<String> parts = user.getParts().stream().map(part -> part.getPart().name()).toList();

        Customer customer = customerService.getCustomerByUserId(user.getId());

        return LoginResponse.builder()
                .token(token)
                .customerId(customer.getId())
                .parts(parts)
                .build();
    }

    private RegisterUserResponse toRegisterResponse(User user){
        List<String> parts = user.getParts().stream().map(part -> part.getPart().name()).toList();

        return RegisterUserResponse.builder()
                .email(user.getEmail())
                .roles(parts)
                .build();
    }
}
