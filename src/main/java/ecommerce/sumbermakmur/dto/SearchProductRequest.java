package ecommerce.sumbermakmur.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchProductRequest {

    private String productName;

    private Long minPrice;

    private Long maxPrice;

    private Integer size;

    private Integer page;
}
