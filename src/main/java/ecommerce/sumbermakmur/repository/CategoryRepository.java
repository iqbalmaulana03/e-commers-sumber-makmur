package ecommerce.sumbermakmur.repository;

import ecommerce.sumbermakmur.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, String>, JpaSpecificationExecutor<Category> {

    Optional<Category> findByName(String name);
}
