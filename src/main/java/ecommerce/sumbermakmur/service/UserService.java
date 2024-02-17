package ecommerce.sumbermakmur.service;

import ecommerce.sumbermakmur.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User loadUserById(String userId);

    void delete(String id);
}
