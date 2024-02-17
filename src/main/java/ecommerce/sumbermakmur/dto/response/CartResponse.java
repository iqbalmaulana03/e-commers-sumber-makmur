package ecommerce.sumbermakmur.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponse {

    private String id;

    private String customerId;

    private String customerName;

    private String productId;

    private String productName;

    private Long price;

    private Integer quantity;
}
