package app.equalityboot.dto.request;

import lombok.Data;

@Data
public class ObjectRequestDto {
    private String name;
    private Float lat;
    private Float lng;
    private String address;
    private String phoneNumber;
    private String email;
    private String description;
}
