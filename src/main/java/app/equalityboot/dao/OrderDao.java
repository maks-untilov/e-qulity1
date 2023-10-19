package app.equalityboot.dao;

import app.equalityboot.model.Order;
import app.equalityboot.model.User;
import app.equalityboot.model.UserWorkDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderDao extends JpaRepository<Order, Long> {
    List<Order> getOrdersByStartTimeGreaterThan(LocalDateTime weekAgo);
    List<Order> getOrderByDate(LocalDate date);

    @Query(value = "from Order o where o.date BETWEEN :startDate AND :endDate")
    List<Order> getOrderByDateBetweenTime(@Param("startDate") LocalDate start, @Param("endDate")LocalDate finish);

    @Query(value = "from Order o order by o.description")
    List<Order> getOrderByDescription();

    @Query(value = "from Order o order by o.object.name")
    List<Order> getOrderByObjectName();
}
