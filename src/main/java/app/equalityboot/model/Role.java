package app.equalityboot.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
@Entity
public class Role {
    public Role(RoleName roleName) {
        this.roleName = roleName;
    }

    public Role() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private RoleName roleName;

    public enum RoleName implements GrantedAuthority {
        ADMIN, USER, MANAGER, BOSS;

        @Override
        public String getAuthority() {
            return name();
        }
    }
}
