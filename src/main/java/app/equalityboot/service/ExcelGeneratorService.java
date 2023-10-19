package app.equalityboot.service;

import app.equalityboot.model.Objects;
import app.equalityboot.model.UserWorkDetails;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface ExcelGeneratorService {
    ByteArrayInputStream dataToExcel(List<UserWorkDetails> list) throws IOException;
    ByteArrayInputStream dataToExcelPassedDays(List<UserWorkDetails> list) throws IOException;
    ByteArrayInputStream dataToUserExcel(List<UserWorkDetails> list) throws IOException;

    ByteArrayInputStream dataFromObjectExcel(List<UserWorkDetails> list, Objects objects) throws IOException;

}
