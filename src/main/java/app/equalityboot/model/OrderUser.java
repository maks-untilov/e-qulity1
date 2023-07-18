package app.equalityboot.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "order_user")
public class OrderUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Order order;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User user;
    private boolean value;
}
