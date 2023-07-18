package app.equalityboot.service.impl;

import app.equalityboot.dao.RoleDao;
import app.equalityboot.model.Role;
import app.equalityboot.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    private RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public Role save(Role role) {
        return roleDao.save(role);
    }

    @Override
    public Role getById(Long id) {
        return roleDao.getReferenceById(id);
    }

    @Override
    public Role getByRoleName(String roleName) {
        return roleDao.getRoleByRoleName(Role.RoleName.valueOf(roleName));
    }
}
