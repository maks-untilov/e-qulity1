package app.equalityboot.dao;

import app.equalityboot.model.Message;
import app.equalityboot.model.Order;
import app.equalityboot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageDao extends JpaRepository<Message, Long> {
    List<Message> getMessagesByFromUser(User fromUser);
    List<Message> getMessagesByToUser(User toUser);
    List<Message> getMessagesByToUserAndFromUser(User fromUser, User toUser);
}
