package ecommerce.sumbermakmur.service;

import ecommerce.sumbermakmur.constant.ERole;
import ecommerce.sumbermakmur.dto.AuthRequest;
import ecommerce.sumbermakmur.dto.response.RegisterUserResponse;
import ecommerce.sumbermakmur.entity.Role;
import ecommerce.sumbermakmur.entity.User;
import ecommerce.sumbermakmur.repository.UserRepository;
import ecommerce.sumbermakmur.service.impl.AuthServiceImpl;
import ecommerce.sumbermakmur.utils.ValidationUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class AuthServiceImplTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private AuthServiceImpl userService;

    @Mock
    private ValidationUtils validationUtils;

    @Mock
    private RoleService roleService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void AuthService_RegisterCustomer_ReturnAuthDto() {

        Role orSave = roleService.getOrSave(ERole.ROLE_CUSTOMER);

        User user = User.builder()
                .email("test@gmail.com")
                .password("testing")
                .parts(List.of(orSave))
                .build();

        AuthRequest request = AuthRequest.builder()
                .email("test@gmail.com")
                .password("testing")
                .build();

        validationUtils.validate(request);

        when(repository.save(Mockito.any(User.class))).thenReturn(user);

        RegisterUserResponse customer = userService.createCustomer(request);

        Assertions.assertThat(customer).isNotNull();
    }
}