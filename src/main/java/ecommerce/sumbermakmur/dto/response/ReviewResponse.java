package ecommerce.sumbermakmur.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewResponse {

    private String id;

    private String customerId;

    private String customerName;

    private String productId;

    private String productName;

    private String comment;

    private Integer rate;

    private Date dateReview;
}
