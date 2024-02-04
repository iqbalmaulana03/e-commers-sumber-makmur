package ecommerce.sumbermakmur.service;

import ecommerce.sumbermakmur.dto.AuthRequest;
import ecommerce.sumbermakmur.dto.response.LoginResponse;
import ecommerce.sumbermakmur.dto.response.RegisterUserResponse;

public interface AuthService {

    RegisterUserResponse createCustomer(AuthRequest request);

    LoginResponse login(AuthRequest request);
}
