package ecommerce.sumbermakmur.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchCategoryRequest {

    private String categoryName;

    private Integer size;

    private Integer page;
}
