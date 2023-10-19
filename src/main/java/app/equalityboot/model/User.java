package app.equalityboot.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.awt.*;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String confirmationToken;
    private Instant tokenCreationDate;
    private boolean isEmailAllowed;
    private String password;
    private String phoneNumber;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Role role;
    private boolean isAllowed;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private User coordinator;
    private String randomColor;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(getRole().getRoleName());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAllowed && isEmailAllowed;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
