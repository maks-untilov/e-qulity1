package app.equalityboot.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class UserWorkDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime startDateTime;
    private LocalDateTime finishDateTime;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private User user;
    private boolean isAccepted;
    private Shift shift;
    private boolean isPaid;
    public enum Shift {
        DAY, NIGHT, NOT_WORKING;
    }
}
