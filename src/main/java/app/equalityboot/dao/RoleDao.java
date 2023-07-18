package app.equalityboot.dao;

import app.equalityboot.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao extends JpaRepository<Role, Long> {
    Role getRoleByRoleName(Role.RoleName roleName);
}
