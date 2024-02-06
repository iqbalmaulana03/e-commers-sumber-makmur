package ecommerce.sumbermakmur.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchCustomerRequest {

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private Integer size;

    private Integer page;
}
