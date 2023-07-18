package app.equalityboot.service;

import app.equalityboot.model.Role;

public interface RoleService {
    Role save(Role role);
    Role getById(Long id);
    Role getByRoleName(String roleName);
}
