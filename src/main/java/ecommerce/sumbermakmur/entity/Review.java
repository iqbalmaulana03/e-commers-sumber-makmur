package ecommerce.sumbermakmur.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_review")
@Builder
public class Review extends DateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer rate;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(name = "date_review")
    private Date dateReview;
}
