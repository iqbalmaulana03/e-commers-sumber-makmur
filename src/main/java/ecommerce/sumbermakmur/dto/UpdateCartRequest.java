package ecommerce.sumbermakmur.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCartRequest {

    @JsonIgnore
    @NotEmpty(message = "Id not has be empty!")
    private String id;

    private String customerId;

    @NotNull(message = "Quantity not be empty!")
    @Size(min = 1, message = "Quantity has be 1")
    private Integer quantity;
}
