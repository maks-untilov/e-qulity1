package app.equalityboot.service;

import app.equalityboot.model.Objects;
import app.equalityboot.model.Order;
import app.equalityboot.model.UserWorkDetails;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface ExcelGeneratorService {
    ByteArrayInputStream dataToExcel(List<UserWorkDetails> list) throws IOException;
    ByteArrayInputStream dataToExcelPassedDays(List<UserWorkDetails> list) throws IOException;
    ByteArrayInputStream dataToUserExcel(List<UserWorkDetails> list) throws IOException;

    ByteArrayInputStream dataFromObjectExcelMonth(List<UserWorkDetails> list, Objects objects) throws IOException;
    ByteArrayInputStream dataFromObjectExcelWeek(List<UserWorkDetails> list, Objects objects, LocalDate startDate) throws IOException;
    public ByteArrayInputStream dataForManager(Order objects) throws IOException;

}
