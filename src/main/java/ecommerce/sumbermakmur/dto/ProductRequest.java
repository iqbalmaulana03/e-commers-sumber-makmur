package ecommerce.sumbermakmur.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {

    @JsonIgnore
    private String id;

    @NotEmpty(message = "Product Name not be empty!")
    private String productName;

    @NotEmpty(message = "Description not be empty")
    private String description;

    @NotNull(message = "price not be null!")
    private Long price;

    @NotBlank(message = "Stock not be blank!")
    private Integer stock;
}
