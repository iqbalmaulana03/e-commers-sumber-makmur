package ecommerce.sumbermakmur.service;

import ecommerce.sumbermakmur.constant.ERole;
import ecommerce.sumbermakmur.entity.Role;

public interface RoleService {

    Role getOrSave(ERole role);
}
