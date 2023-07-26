package app.equalityboot.service.impl;

import app.equalityboot.dao.OrderUserDao;
import app.equalityboot.model.Order;
import app.equalityboot.model.OrderUser;
import app.equalityboot.model.User;
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

    @Override
    public List<OrderUser> getByCoordinator(List<OrderUser> allUsersPerOrder, User user) {
        return allUsersPerOrder.stream()
                .filter(orderUser -> orderUser.getUser() != null && orderUser.getUser().getCoordinator() != null)
                .filter(orderUser -> orderUser.getUser().getCoordinator().equals(user))
                .toList();
    }
}
