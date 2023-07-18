package app.equalityboot.dto.request;

import app.equalityboot.model.Role;
import lombok.Data;

import java.util.List;

@Data
public class UserRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private Integer uniqueNumber;
    private Role role;
    private boolean isAllowed;
}
