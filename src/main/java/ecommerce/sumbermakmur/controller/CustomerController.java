package ecommerce.sumbermakmur.controller;

import ecommerce.sumbermakmur.dto.CustomerRequest;
import ecommerce.sumbermakmur.dto.SearchCustomerRequest;
import ecommerce.sumbermakmur.dto.response.CustomerResponse;
import ecommerce.sumbermakmur.dto.response.PagingResponse;
import ecommerce.sumbermakmur.dto.response.WebResponse;
import ecommerce.sumbermakmur.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PutMapping("/{customerId}")
    public ResponseEntity<WebResponse<CustomerResponse>> update(@PathVariable("customerId") String id,
                                                                @RequestBody CustomerRequest request){
        request.setId(id);
        CustomerResponse update = customerService.update(request);

        WebResponse<CustomerResponse> response = WebResponse.<CustomerResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully update customer")
                .data(update)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<WebResponse<CustomerResponse>> get(@PathVariable("customerId") String id){
        CustomerResponse customerResponse = customerService.get(id);

        WebResponse<CustomerResponse> response = WebResponse.<CustomerResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully get customer by id")
                .data(customerResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<WebResponse<String>> delete(@PathVariable("customerId") String id){
        customerService.delete(id);

        WebResponse<String> response = WebResponse.<String>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully delete customer")
                .data("OK")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<WebResponse<List<CustomerResponse>>> search(@RequestParam(name = "firstName", required = false)String firstName,
                                                         @RequestParam(name = "lastName", required = false)String lastName,
                                                         @RequestParam(name = "phoneNumber", required = false)String phoneNumber,
                                                         @RequestParam(name = "page", defaultValue = "1")Integer page,
                                                         @RequestParam(name = "size", defaultValue = "10")Integer size){

        SearchCustomerRequest request = SearchCustomerRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .page(page)
                .size(size)
                .build();

        Page<CustomerResponse> search = customerService.search(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .page(request.getPage())
                .size(size)
                .totalPage(search.getTotalPages())
                .totalElements(search.getTotalElements())
                .build();

        WebResponse<List<CustomerResponse>> response = WebResponse.<List<CustomerResponse>>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully get customer")
                .data(search.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/{customerId}/upload-avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<WebResponse<CustomerResponse>> uploadAvatar(@PathVariable("customerId") String id,
                                                                      @RequestParam("avatarFileName")MultipartFile avatarFileName) throws IOException {
        CustomerResponse customerResponse = customerService.uploadAvatar(avatarFileName, id);

        WebResponse<CustomerResponse> response = WebResponse.<CustomerResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully upload avatar customer")
                .data(customerResponse)
                .build();
        return ResponseEntity.ok(response);
    }
}
