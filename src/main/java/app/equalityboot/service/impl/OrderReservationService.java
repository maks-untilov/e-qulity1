package app.equalityboot.service.impl;

import app.equalityboot.model.Objects;
import app.equalityboot.model.Order;
import app.equalityboot.model.OrderUser;
import app.equalityboot.model.User;
import app.equalityboot.service.ObjectsService;
import app.equalityboot.service.OrderService;
import app.equalityboot.service.OrderUserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderReservationService {
    private OrderService orderService;
    private ObjectsService objectsService;
    private OrderUserService orderUserService;

    public OrderReservationService(OrderService orderService, ObjectsService objectsService, OrderUserService orderUserService) {
        this.orderService = orderService;
        this.objectsService = objectsService;
        this.orderUserService = orderUserService;
    }

    public Order reserve(String objectName, LocalDateTime startTime, LocalDateTime finishTime,
                         Integer needWorkers, List<User> workers, String description) {
        Order order = new Order();
        Objects objectsByName = objectsService.getObjectsByName(objectName);
        order.setObject(objectsByName);
        order.setStartTime(startTime);
        order.setFinishTime(finishTime);
        order.setDescription(description);
        order.setDate(startTime.toLocalDate());
        order.setNeedWorkers(needWorkers);
        orderService.save(order);
        OrderUser orderUser = new OrderUser();
        orderUser.setOrder(order);
        orderUserService.save(orderUser);
        return order;
    }
}
