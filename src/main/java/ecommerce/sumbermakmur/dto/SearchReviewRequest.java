package ecommerce.sumbermakmur.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchReviewRequest {

    private Integer rate;

    private Integer page;

    private Integer size;
}
