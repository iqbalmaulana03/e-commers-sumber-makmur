package ecommerce.sumbermakmur.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "m_product")
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

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Category> categories;
}
