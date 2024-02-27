package ecommerce.sumbermakmur.controller;

import com.midtrans.httpclient.error.MidtransError;
import ecommerce.sumbermakmur.dto.OrderRequest;
import ecommerce.sumbermakmur.dto.response.OrderResponse;
import ecommerce.sumbermakmur.dto.response.WebResponse;
import ecommerce.sumbermakmur.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;

    @PostMapping("/")
    public ResponseEntity<WebResponse<OrderResponse>> create(@RequestBody OrderRequest request) throws MidtransError {
        OrderResponse orderResponse = service.create(request);

        WebResponse<OrderResponse> response = WebResponse.<OrderResponse>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("successfully create order")
                .data(orderResponse)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<WebResponse<OrderResponse>> get(@PathVariable("customerId") String id){
        OrderResponse orderResponse = service.get(id);

        WebResponse<OrderResponse> response = WebResponse.<OrderResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully get order by customer id")
                .data(orderResponse)
                .build();

        return ResponseEntity.ok(response);
    }
}
