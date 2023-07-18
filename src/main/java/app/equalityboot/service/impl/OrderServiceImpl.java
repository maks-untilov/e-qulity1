package app.equalityboot.service.impl;

import app.equalityboot.dao.OrderDao;
import app.equalityboot.model.Order;
import app.equalityboot.service.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private OrderDao orderDao;

    public OrderServiceImpl(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public Order save(Order order) {
        return orderDao.save(order);
    }

    @Override
    public Order getById(Long id) {
        return orderDao.getReferenceById(id);
    }

    @Override
    public List<Order> getAll() {
        return orderDao.findAll();
    }

    @Override
    public List<Order> getByDateGreaterThan(LocalDateTime time) {
        return orderDao.getOrdersByStartTimeGreaterThan(time);
    }

    @Override
    public void deleteOrder(Order order) {
        orderDao.delete(order);
    }

    @Override
    public List<Order> getByDate(LocalDate date) {
        return orderDao.getOrderByDate(date);
    }
}
