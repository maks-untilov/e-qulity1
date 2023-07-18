package app.equalityboot.service.impl;

import app.equalityboot.dao.OrderUserDao;
import app.equalityboot.model.Order;
import app.equalityboot.model.OrderUser;
import app.equalityboot.service.OrderUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderUserServiceImpl implements OrderUserService {
    private OrderUserDao orderUserDao;

    public OrderUserServiceImpl(OrderUserDao orderUserDao) {
        this.orderUserDao = orderUserDao;
    }

    @Override
    public OrderUser save(OrderUser orderUser) {
        return orderUserDao.save(orderUser);
    }

    @Override
    public List<OrderUser> getByOrder(Order order) {
        return orderUserDao.getOrderUsersByOrder(order);
    }
}
