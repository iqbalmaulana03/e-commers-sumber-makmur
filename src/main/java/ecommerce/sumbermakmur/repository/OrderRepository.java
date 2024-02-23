package ecommerce.sumbermakmur.repository;

import ecommerce.sumbermakmur.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {
}
