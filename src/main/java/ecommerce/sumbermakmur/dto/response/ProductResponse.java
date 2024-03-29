package ecommerce.sumbermakmur.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {

    private String id;

    private String productName;

    private String description;

    private Long price;

    private Integer stock;

    private List<ProductDetail> responses;
}
