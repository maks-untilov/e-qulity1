package app.equalityboot.service;

import app.equalityboot.model.Order;
import app.equalityboot.model.OrderUser;
import app.equalityboot.model.User;

import java.util.List;

public interface OrderUserService {
    OrderUser save(OrderUser orderUser);
    List<OrderUser> getByOrder(Order order);
    List<OrderUser> getByCoordinator(List<OrderUser> allUsersPerOrder, User user);
}
