package ecommerce.sumbermakmur.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagingResponse {

    private Integer size;

    private Integer page;

    private Integer totalPage;

    private Long totalElements;
}
