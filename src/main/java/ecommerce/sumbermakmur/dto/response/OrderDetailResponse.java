package ecommerce.sumbermakmur.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderDetailResponse {

    private String orderDetailId;

    private String cartId;

    private Long price;

    private Integer quantity;

    private String productId;

    private String productName;

    private Long countPrice;
}
