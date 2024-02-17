package ecommerce.sumbermakmur.repository;

import ecommerce.sumbermakmur.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, String> {

    Optional<Cart> findByCustomerId(String id);
}
