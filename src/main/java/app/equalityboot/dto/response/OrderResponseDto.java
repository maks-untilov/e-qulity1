package app.equalityboot.dto.response;

import app.equalityboot.model.Objects;
import app.equalityboot.model.User;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDto {
    private Long id;
    private Objects object;
    private LocalDateTime startDate;
    private LocalDateTime finishDate;
    private List<User> workers;
}
