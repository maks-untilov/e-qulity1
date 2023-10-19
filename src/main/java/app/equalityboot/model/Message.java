package app.equalityboot.model;

import jakarta.persistence.*;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User fromUser;
    @ManyToOne
    private User toUser;
    private String message;
}
