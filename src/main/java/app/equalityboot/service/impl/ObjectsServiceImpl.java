package app.equalityboot.service.impl;

import app.equalityboot.dao.ObjectsDao;
import app.equalityboot.model.Objects;
import app.equalityboot.service.ObjectsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObjectsServiceImpl implements ObjectsService {
    private final ObjectsDao objectsDao;

    public ObjectsServiceImpl(ObjectsDao objectsDao) {
        this.objectsDao = objectsDao;
    }

    @Override
    public Objects add(Objects objects) {
        return objectsDao.save(objects);
    }

    @Override
    public Objects get(Long id) {
        return objectsDao.getReferenceById(id);
    }

    @Override
    public List<Objects> gelAll() {
        return objectsDao.findAll();
    }

    @Override
    public Objects getObjectsByName(String object_name) {
        return objectsDao.getObjectsByName(object_name);
    }
}
