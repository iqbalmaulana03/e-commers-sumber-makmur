package ecommerce.sumbermakmur.controller;

import ecommerce.sumbermakmur.dto.CartRequest;
import ecommerce.sumbermakmur.dto.UpdateCartRequest;
import ecommerce.sumbermakmur.dto.response.CartResponse;
import ecommerce.sumbermakmur.dto.response.WebResponse;
import ecommerce.sumbermakmur.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService service;

    @PostMapping("/")
    public ResponseEntity<WebResponse<CartResponse>> create(@RequestBody CartRequest request){
        CartResponse cartResponse = service.create(request);

        WebResponse<CartResponse> response = WebResponse.<CartResponse>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("successfully create cart")
                .data(cartResponse)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<WebResponse<CartResponse>> get(@PathVariable("customerId") String id){
        CartResponse byCustomer = service.getByCustomer(id);

        WebResponse<CartResponse> response = WebResponse.<CartResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully get cart by customer id")
                .data(byCustomer)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<WebResponse<CartResponse>> update(@PathVariable("customerId") String id,
                                                            @RequestBody UpdateCartRequest request){
        request.setId(id);
        CartResponse byCustomer = service.update(request);

        WebResponse<CartResponse> response = WebResponse.<CartResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully updated cart")
                .data(byCustomer)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<WebResponse<String>> delete(@PathVariable("cartId") String id){
        service.delete(id);

        WebResponse<String> response = WebResponse.<String>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully delete cart")
                .data("OK")
                .build();

        return ResponseEntity.ok(response);
    }
}
