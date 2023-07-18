package app.equalityboot.dao;

import app.equalityboot.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderDao extends JpaRepository<Order, Long> {
    List<Order> getOrdersByStartTimeGreaterThan(LocalDateTime weekAgo);
    List<Order> getOrderByDate(LocalDate date);
}
