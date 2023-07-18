package app.equalityboot.service;

import app.equalityboot.model.Objects;

import java.util.List;

public interface ObjectsService {
    Objects add(Objects objects);
    Objects get(Long id);
    List<Objects> gelAll();
    Objects getObjectsByName(String object_name);
}
