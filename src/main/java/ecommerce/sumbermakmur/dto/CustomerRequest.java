package ecommerce.sumbermakmur.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerRequest {

    @JsonIgnore
    private String id;

    @NotEmpty(message = "firstName not be empty!")
    private String firstName;

    @NotEmpty(message = "lastName not be empty!")
    private String lastName;

    @NotEmpty(message = "address not be empty!")
    private String address;

    @NotEmpty(message = "phoneNumber not be empty!")
    @JsonFormat(pattern = "^08[0-9]{8,11}$")
    @Size(max = 13, message = "phoneNumber max 13")
    private String phoneNumber;
}
