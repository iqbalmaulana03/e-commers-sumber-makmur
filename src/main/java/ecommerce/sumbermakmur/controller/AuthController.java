package ecommerce.sumbermakmur.controller;

import ecommerce.sumbermakmur.dto.AuthRequest;
import ecommerce.sumbermakmur.dto.response.LoginResponse;
import ecommerce.sumbermakmur.dto.response.RegisterUserResponse;
import ecommerce.sumbermakmur.dto.response.WebResponse;
import ecommerce.sumbermakmur.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/registerCustomer")
    public ResponseEntity<WebResponse<RegisterUserResponse>> registerCustomer(@RequestBody AuthRequest request){
        RegisterUserResponse customer = service.createCustomer(request);

        WebResponse<RegisterUserResponse> response = WebResponse.<RegisterUserResponse>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("successfully create customer")
                .data(customer)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<WebResponse<LoginResponse>> login(@RequestBody AuthRequest request){
        LoginResponse login = service.login(request);

        WebResponse<LoginResponse> response = WebResponse.<LoginResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully login")
                .data(login)
                .build();

        return ResponseEntity.ok(response);
    }
}
