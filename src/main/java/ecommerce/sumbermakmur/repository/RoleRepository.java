package ecommerce.sumbermakmur.repository;

import ecommerce.sumbermakmur.constant.ERole;
import ecommerce.sumbermakmur.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {

    Optional<Role> findByPart(ERole role);
}
