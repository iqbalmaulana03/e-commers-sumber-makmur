package ecommerce.sumbermakmur.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartRequest {

    @NotEmpty(message = "Product Id not empty!")
    private String productId;

    @NotNull(message = "Quantity not be empty!")
    private Integer quantity;
}
