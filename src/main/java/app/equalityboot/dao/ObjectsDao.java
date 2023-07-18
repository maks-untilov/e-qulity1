package app.equalityboot.dao;

import app.equalityboot.model.Objects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectsDao extends JpaRepository<Objects, Long> {
    Objects getObjectsByName(String object_name);
}