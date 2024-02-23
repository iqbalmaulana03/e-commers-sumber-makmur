package ecommerce.sumbermakmur.repository;

import ecommerce.sumbermakmur.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
}
