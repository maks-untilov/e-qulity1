package app.equalityboot.service;

import org.springframework.stereotype.Service;

public interface ValidationService {
    boolean validateLatin(String word);
    String validateAllLatin(String first_name, String last_name,
                            String number_id, String sex,
                            String birth_date, String dane_kontaktowe,
                            String education, String pesel,
                            String osoba, String location);
}
