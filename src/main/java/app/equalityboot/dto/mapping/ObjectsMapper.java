package app.equalityboot.dto.mapping;

import app.equalityboot.dto.request.ObjectRequestDto;
import app.equalityboot.dto.response.ObjectResponseDto;
import app.equalityboot.model.Objects;
import org.springframework.stereotype.Component;

@Component
public class ObjectsMapper {
    public ObjectResponseDto toResponseDto(Objects objects) {
        ObjectResponseDto objectsResponseDto = new ObjectResponseDto();
        objectsResponseDto.setId(objects.getId());
        objectsResponseDto.setName(objects.getName());
        objectsResponseDto.setEmail(objects.getEmail());
        objectsResponseDto.setPhoneNumber(objects.getPhoneNumber());
        objectsResponseDto.setLat(objects.getLat());
        objectsResponseDto.setLng(objects.getLng());
        objectsResponseDto.setDescription(objects.getDescription());
        objectsResponseDto.setAddress(objects.getAddress());
        return objectsResponseDto;
    }

    public Objects toModel(ObjectRequestDto objectRequestDto) {
        Objects objects = new Objects();
        objects.setName(objectRequestDto.getName());
        objects.setPhoneNumber(objectRequestDto.getPhoneNumber());
        objects.setEmail(objectRequestDto.getEmail());
        objects.setDescription(objectRequestDto.getDescription());
        objects.setLng(objectRequestDto.getLng());
        objects.setLat(objectRequestDto.getLat());
        objects.setAddress(objectRequestDto.getAddress());
        return objects;
    }
}
