package ecommerce.sumbermakmur.repository;

import ecommerce.sumbermakmur.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, String> {
}
