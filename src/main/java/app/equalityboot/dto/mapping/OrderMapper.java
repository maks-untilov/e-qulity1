package app.equalityboot.dto.mapping;

import app.equalityboot.dto.request.OrderRequestDto;
import app.equalityboot.dto.response.OrderResponseDto;
import app.equalityboot.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    public OrderResponseDto toDtoResponse(Order order) {
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setId(order.getId());
        orderResponseDto.setObject(order.getObject());
        orderResponseDto.setStartDate(order.getStartTime());
        orderResponseDto.setFinishDate(order.getStartTime());
        return orderResponseDto;
    }

    public Order toModel(OrderRequestDto orderRequestDto) {
        Order order = new Order();
        order.setObject(orderRequestDto.getObject());
        order.setStartTime(orderRequestDto.getStartDate());
        order.setFinishTime(orderRequestDto.getFinishTime());
        return order;
    }
}
