package app.equalityboot.dto.request;

import app.equalityboot.model.Objects;
import app.equalityboot.model.User;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderRequestDto {
    private Objects object;
    private LocalDateTime startDate;
    private LocalDateTime finishTime;
    private List<User> workers;
}
