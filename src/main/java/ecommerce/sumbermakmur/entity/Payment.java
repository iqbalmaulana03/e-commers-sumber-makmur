package ecommerce.sumbermakmur.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_payment")
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "method_payment")
    private String methodPayment;

    @Column(name = "total_payment")
    private Long totalPayment;

    @Column(name = "date_payment")
    private Date datePayment;

    @Column(name = "link_payment")
    private String linkPayment;

    @OneToOne
    @JoinColumn(name = "order_detail_id")
    private OrderDetail orderDetail;
}
