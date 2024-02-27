package ecommerce.sumbermakmur.repository;

import ecommerce.sumbermakmur.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {

    Optional<Order> findByCustomerId(String customerId);
}
