package app.equalityboot.dto.response;

import app.equalityboot.model.Role;
import lombok.Data;

import java.util.List;

@Data
public class UserResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private Integer uniqueNumber;
    private Role role;
    private boolean isAllowed;
}
