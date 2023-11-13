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
    private Order order;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private User user;
    private boolean isMissed;
    private boolean isAccepted;
    private Shift shift;
    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] image;
    private String imageName;
    private boolean isPaid;
    public enum Shift {
        DAY, NIGHT, NOT_WORKING, DAY_NIGHT;
    }
}
