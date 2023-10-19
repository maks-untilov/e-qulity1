package app.equalityboot.service.impl;

import app.equalityboot.service.ValidationService;
import org.springframework.stereotype.Service;

@Service
public class ValidationServiceImpl implements ValidationService {
    private String isLatinPattern = "^[a-zA-Z0-9\\p{L}\\p{N}._%+-]+$";

    @Override
    public boolean validateLatin(String word) {
        if (null == word || word.trim().length() <= 0)
            return false;
        return word.matches(isLatinPattern);
    }

    @Override
    public String validateAllLatin(String first_name, String last_name, String number_id,
                                   String sex, String birth_date, String dane_kontaktowe,
                                   String education, String pesel, String osoba,
                                   String location) {
        String word = "";

        if (!validateLatin(first_name)) {
            word = "IMIĘ";
        } else if (!validateLatin(last_name)) {
            word = "NAZWISKO";
        } else if (!validateLatin(number_id)) {
            word = "ID KOORDYNATOR";
        } else if (!validateLatin(sex)) {
            word = "PAN/PANI";
        } else if (!validateLatin(birth_date)) {
            word = "DATA URODZENIA";
        } else if (!validateLatin(dane_kontaktowe)) {
            word = "DANE KONTAKTOWE";
        } else if (!validateLatin(education)) {
            word = "WYKSZTALCENIE";
        } else if (!validateLatin(pesel)) {
            word = "PESEL ";
        } else if (!validateLatin(osoba)) {
            word = "OSOBA, KTÓRĄ NALEŻY ZAWIADOMIĆ W RAZIE WYPADKU";
        } else if (!validateLatin(location)) {
            word = "MIEJSCE ZAMIESZKANIA";
        }

        return word;
    }
}
