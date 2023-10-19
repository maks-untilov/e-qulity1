package app.equalityboot.service;

import app.equalityboot.model.Message;
import app.equalityboot.model.User;

import java.util.List;

public interface MessageService {
    List<Message> getMessagesByFromUser(User fromUser);
    List<Message> getMessagesByToUser(User toUser);
    List<Message> getMessagesByToUserAndFromUser(User fromUser, User toUser);
}
