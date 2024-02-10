package ecommerce.sumbermakmur.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "m_product")
@Builder
public class Product extends DateTime{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name_product")
    private String nameProduct;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Long price;

    private Integer stock;
}
