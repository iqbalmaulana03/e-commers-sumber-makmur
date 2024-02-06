package ecommerce.sumbermakmur.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "m_customers")
@Builder
public class Customer extends DateTime{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "avatar_file_name")
    private String avatarFileName;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
