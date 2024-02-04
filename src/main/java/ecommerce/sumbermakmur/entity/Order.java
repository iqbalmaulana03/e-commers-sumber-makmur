package ecommerce.sumbermakmur.entity;

import ecommerce.sumbermakmur.constant.EOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_order")
public class Order extends DateTime{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "date_order")
    private Date dateOrder;

    @Enumerated(EnumType.STRING)
    private EOrder status;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
