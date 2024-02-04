package ecommerce.sumbermakmur.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ecommerce.sumbermakmur.constant.ERole;
import ecommerce.sumbermakmur.dto.AuthRequest;
import ecommerce.sumbermakmur.entity.Role;
import ecommerce.sumbermakmur.entity.User;
import ecommerce.sumbermakmur.service.AuthService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private  User user;

    private  Role role;

    private AuthRequest request;

    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @BeforeEach
    void setUp() {
        user = User.builder().email("test@gmail.com").password("testing").build();
        request = AuthRequest.builder().email("test@gmail.com").password("testing").build();
        role = Role.builder().part(ERole.ROLE_CUSTOMER).build();
    }

    @Test
    void AuthController_RegisterCustomer_ReturnCreated() throws Exception {
        given(authService.createCustomer(ArgumentMatchers.any())).willAnswer((invocation -> invocation.getArgument(0)));

        ResultActions response = mockMvc.perform(post("api/v1/auth/registerCustomer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(request.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password", CoreMatchers.is(request.getPassword())));
    }
}