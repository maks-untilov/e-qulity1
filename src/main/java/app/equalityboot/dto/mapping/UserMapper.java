package app.equalityboot.dto.mapping;

import app.equalityboot.dto.request.UserRequestDto;
import app.equalityboot.dto.response.UserResponseDto;
import app.equalityboot.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponseDto toResponseDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setRole(user.getRole());
        userResponseDto.setAllowed(user.isAllowed());
        userResponseDto.setPassword(user.getPassword());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setLastName(user.getLastName());
        userResponseDto.setFirstName(user.getFirstName());
        return userResponseDto;
    }

    public User toModel(UserRequestDto userRequestDto) {
        User user = new User();
        user.setPhoneNumber(userRequestDto.getPhoneNumber());
        user.setEmail(userRequestDto.getEmail());
        user.setRole(userRequestDto.getRole());
        user.setPassword(userRequestDto.getPassword());
        user.setLastName(userRequestDto.getLastName());
        user.setFirstName(userRequestDto.getFirstName());
        return user;
    }
}
