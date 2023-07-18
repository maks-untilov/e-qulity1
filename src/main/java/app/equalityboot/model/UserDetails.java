package app.equalityboot.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private User user;
    private String sex;
    private String birthDate;
    private String education;
    private String pesel;
    private String osoba;
    private String location;
    private String contactData;
}
