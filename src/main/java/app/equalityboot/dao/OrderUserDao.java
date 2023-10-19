package app.equalityboot.dao;

import app.equalityboot.model.Order;
import app.equalityboot.model.OrderUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderUserDao extends JpaRepository<OrderUser, Long> {
    List<OrderUser> getOrderUsersByOrder(Order order);
    OrderUser getOrderUsersById(Long id);
}
