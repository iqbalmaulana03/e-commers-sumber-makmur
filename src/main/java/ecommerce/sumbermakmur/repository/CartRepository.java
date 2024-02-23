package ecommerce.sumbermakmur.repository;

import ecommerce.sumbermakmur.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, String> {

    List<Cart> findByCustomerId(String id);
}
