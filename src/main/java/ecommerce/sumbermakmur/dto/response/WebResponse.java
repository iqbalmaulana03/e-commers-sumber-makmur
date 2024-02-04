package ecommerce.sumbermakmur.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebResponse<T> {

    private String message;

    private String status;

    private T data;
}
