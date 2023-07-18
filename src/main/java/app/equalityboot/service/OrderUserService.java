package app.equalityboot.service;

import app.equalityboot.model.Order;
import app.equalityboot.model.OrderUser;

import java.util.List;

public interface OrderUserService {
    OrderUser save(OrderUser orderUser);
    List<OrderUser> getByOrder(Order order);
}
