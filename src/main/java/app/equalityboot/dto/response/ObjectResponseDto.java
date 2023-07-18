package app.equalityboot.dto.response;

import lombok.Data;

@Data
public class ObjectResponseDto {
    private Long id;
    private String name;
    private Float lat;
    private Float lng;
    private String address;
    private String phoneNumber;
    private String email;
    private String description;
}
