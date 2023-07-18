package app.equalityboot.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name="customer_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne()
    private Objects object;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
    private LocalDate date;
    private String description;
    private int needWorkers;
}
