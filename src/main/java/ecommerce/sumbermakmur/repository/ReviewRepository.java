package ecommerce.sumbermakmur.repository;

import ecommerce.sumbermakmur.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ReviewRepository extends JpaRepository<Review, String>, JpaSpecificationExecutor<Review> {
}
