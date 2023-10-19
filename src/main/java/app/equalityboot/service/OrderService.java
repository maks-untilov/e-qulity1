package app.equalityboot.service;

import app.equalityboot.model.Order;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    Order save(Order order);
    Order getById(Long id);
    List<Order> getAll();
    List<Order> getByDateGreaterThan(LocalDateTime time);
    void deleteOrder(Order order);
    List<Order> getByDate(LocalDate date);
    List<Order> getOrderByDateBetween(LocalDate startDate, LocalDate finishDate);
    List<Order> getOrderByDescription();
    List<Order> getOrderByObjectName();
}
