package ecommerce.sumbermakmur.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderResponse {

    private String id;

    private Date dateOrder;

    private String status;

    private String idCustomer;

    private String customerName;

    private List<OrderDetailResponse> orderDetailResponses;
}
