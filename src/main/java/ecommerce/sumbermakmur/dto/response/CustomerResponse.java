package ecommerce.sumbermakmur.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerResponse {

    private String id;

    private String firstName;

    private String lastName;

    private String address;

    private String phoneNumber;

    private String avatarFileName;
}
