package ecommerce.sumbermakmur.repository;

import ecommerce.sumbermakmur.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, String>, JpaSpecificationExecutor<Customer> {

    Optional<Customer> findByUserId(String userId);
}
