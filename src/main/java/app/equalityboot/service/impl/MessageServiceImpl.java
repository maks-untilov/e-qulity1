package app.equalityboot.service.impl;

import app.equalityboot.dao.MessageDao;
import app.equalityboot.model.Message;
import app.equalityboot.model.User;
import app.equalityboot.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    private final MessageDao messageDao;

    public MessageServiceImpl(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    @Override
    public List<Message> getMessagesByFromUser(User fromUser) {
        return messageDao.getMessagesByFromUser(fromUser);
    }

    @Override
    public List<Message> getMessagesByToUser(User toUser) {
        return messageDao.getMessagesByToUser(toUser);
    }

    @Override
    public List<Message> getMessagesByToUserAndFromUser(User fromUser, User toUser) {
        return messageDao.getMessagesByToUserAndFromUser(fromUser, toUser);
    }
}
