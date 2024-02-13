package ecommerce.sumbermakmur.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewRequest {

    @NotEmpty(message = "Product Id not empty!")
    private String productId;

    private String comment;

    @NotNull(message = "Rate not be null")
    @Size(max = 5, message = "Rate Max be 5")
    private Integer rate;
}
