package ecommerce.sumbermakmur.service.impl;

import ecommerce.sumbermakmur.constant.ERole;
import ecommerce.sumbermakmur.entity.Role;
import ecommerce.sumbermakmur.repository.RoleRepository;
import ecommerce.sumbermakmur.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Role getOrSave(ERole role) {

        Optional<Role> optionalRole = repository.findByPart(role);

        if (optionalRole.isPresent()) return optionalRole.get();

        Role roles = Role.builder()
                .part(role)
                .build();

        return repository.save(roles);
    }
}
