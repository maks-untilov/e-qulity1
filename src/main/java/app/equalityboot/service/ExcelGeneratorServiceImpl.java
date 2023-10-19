package app.equalityboot.service;

import app.equalityboot.model.Objects;
import app.equalityboot.model.User;
import app.equalityboot.model.UserWorkDetails;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ExcelGeneratorServiceImpl implements ExcelGeneratorService {
    private final String ALL_EXCEL_NAME = "excel_all";
    private final UserService userService;
    private final OrderUserService orderUserService;
    private final OrderService orderService;
    private final ObjectsService objectsService;

    public ExcelGeneratorServiceImpl(UserService userService, OrderUserService orderUserService,
                                     OrderService orderService, ObjectsService objectsService) {
        this.userService = userService;
        this.orderUserService = orderUserService;
        this.orderService = orderService;
        this.objectsService = objectsService;
    }

    public ByteArrayInputStream dataToExcel(List<UserWorkDetails> list) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            Sheet sheet = workbook.createSheet(ALL_EXCEL_NAME);

            // Создайте строку для заголовка
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Працівники");

            LocalDate localDate = list.get(0).getStartDateTime().toLocalDate();
            LocalDateTime dateToExcelStartMonth = LocalDate.of(localDate.getYear(),
                    localDate.getMonth(), 1).atTime(0, 0);
            LocalDateTime dateToExcelFinishMonth = LocalDate.of(localDate.getYear(),
                    localDate.getMonth(),
                    localDate.getMonth().length(localDate.isLeapYear())).atTime(23, 59, 59);

            // Получите список уникальных дат
            List<LocalDate> dates = new ArrayList<>();
            LocalDate currentDate = dateToExcelStartMonth.toLocalDate();

            while (!currentDate.isAfter(dateToExcelFinishMonth.toLocalDate())) {
                dates.add(currentDate);
                currentDate = currentDate.plusDays(1);
            }

            // Создайте заголовки для дат
            int i;
            for (i = 0; i < dates.size(); i++) {
                headerRow.createCell(i + 1).setCellValue(dates.get(i).toString());
            }

            headerRow.createCell(i + 1).setCellValue("Разом");

            // Создайте множество для хранения уникальных пользователей

            List<User> users = userService.getAll();

            Set<User> uniqueUsers = new HashSet<>(users);

            int rowIndex = 1;
            for (User user : uniqueUsers) {
                Row dataRow = sheet.createRow(rowIndex);
                dataRow.createCell(0).setCellValue(user.getFirstName() + " " + user.getLastName());

                // Наполните ячейки данными о времени работы
                for (int j = 0; j < dates.size(); j++) {
                    LocalDate date = dates.get(j);
                    List<UserWorkDetails> userWorkDetailsForDate = list.stream()
                            .filter(details -> details.getUser().getId().equals(user.getId())
                                    && details.getStartDateTime().toLocalDate().equals(date)
                                    && !details.isMissed())
                            .toList();

                    // Вычислите суммарное время работы на эту дату
                    Duration totalWorkDuration = Duration.ZERO;
                    for (UserWorkDetails details : userWorkDetailsForDate) {
                        totalWorkDuration = totalWorkDuration.plus(Duration.between(details.getStartDateTime(), details.getFinishDateTime()));
                    }
                    // Форматируйте длительность в формат HH:mm
                    String totalWorkHours = totalWorkDuration.isZero() ? "-" : String.format("%02d:%02d",
                            totalWorkDuration.toHours(),
                            totalWorkDuration.toMinutesPart());

                    dataRow.createCell(j + 1).setCellValue(totalWorkHours);
                }

// Добавляем столбец "Разом"
                List<UserWorkDetails> userWorkDetailsForRange = list.stream()
                        .filter(details -> details.getUser().getId().equals(user.getId())
                                && details.getStartDateTime().toLocalDate().isAfter(dateToExcelStartMonth.toLocalDate())
                                && details.getStartDateTime().toLocalDate().isBefore(dateToExcelFinishMonth.toLocalDate())
                                && !details.isMissed())
                        .toList();

                Duration totalWorkDurationForRange = Duration.ZERO;
                for (UserWorkDetails details : userWorkDetailsForRange) {
                    totalWorkDurationForRange = totalWorkDurationForRange.plus(Duration.between(details.getStartDateTime(), details.getFinishDateTime()));
                }
                float totalWorkHoursForRange = (float) totalWorkDurationForRange.toMinutes() / 60;
                dataRow.createCell(dates.size() + 1).setCellValue(String.format("%.2f", totalWorkHoursForRange));


                rowIndex++;
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Can't write data to Excel", e);
        } finally {
            workbook.close();
            out.close();
        }
    }

    public ByteArrayInputStream dataToExcelPassedDays(List<UserWorkDetails> list) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            Sheet sheet = workbook.createSheet(ALL_EXCEL_NAME);

            // Создайте строку для заголовка
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Працівники");

            LocalDate localDate = list.get(0).getStartDateTime().toLocalDate();
            LocalDateTime dateToExcelStartMonth = LocalDate.of(localDate.getYear(),
                    localDate.getMonth(), 1).atTime(0, 0);
            LocalDateTime dateToExcelFinishMonth = LocalDate.of(localDate.getYear(),
                    localDate.getMonth(),
                    localDate.getMonth().length(localDate.isLeapYear())).atTime(23, 59, 59);

            // Получите список уникальных дат
            List<LocalDate> dates = new ArrayList<>();
            LocalDate currentDate = dateToExcelStartMonth.toLocalDate();

            while (!currentDate.isAfter(dateToExcelFinishMonth.toLocalDate())) {
                dates.add(currentDate);
                currentDate = currentDate.plusDays(1);
            }

            // Создайте заголовки для дат
            int i;
            for (i = 0; i < dates.size(); i++) {
                headerRow.createCell(i + 1).setCellValue(dates.get(i).toString());
            }

            headerRow.createCell(i + 1).setCellValue("Разом");

            // Создайте множество для хранения уникальных пользователей

            List<User> users = userService.getAll();

            Set<User> uniqueUsers = new HashSet<>(users);

            int rowIndex = 1;
            for (User user : uniqueUsers) {
                Row dataRow = sheet.createRow(rowIndex);
                dataRow.createCell(0).setCellValue(user.getFirstName() + " " + user.getLastName());

                // Наполните ячейки данными о времени работы
                for (int j = 0; j < dates.size(); j++) {
                    LocalDate date = dates.get(j);
                    List<UserWorkDetails> userWorkDetailsForDate = list.stream()
                            .filter(details -> details.getUser().getId().equals(user.getId())
                                    && details.getStartDateTime().toLocalDate().equals(date)
                                    && !details.isMissed())
                            .toList();

                    // Вычислите суммарное время работы на эту дату
                    Duration totalWorkDuration = Duration.ZERO;
                    for (UserWorkDetails details : userWorkDetailsForDate) {
                        totalWorkDuration = totalWorkDuration.plus(Duration.between(details.getStartDateTime(), details.getFinishDateTime()));
                    }
                    // Форматируйте длительность в формат HH:mm
                    String totalWorkHours = totalWorkDuration.isZero() ? "-" : String.format("%02d:%02d",
                            totalWorkDuration.toHours(),
                            totalWorkDuration.toMinutesPart());

                    dataRow.createCell(j + 1).setCellValue(totalWorkHours);
                }

// Добавляем столбец "Разом"
                List<UserWorkDetails> userWorkDetailsForRange = list.stream()
                        .filter(details -> details.getUser().getId().equals(user.getId())
                                && details.getStartDateTime().toLocalDate().isAfter(dateToExcelStartMonth.toLocalDate())
                                && details.getStartDateTime().toLocalDate().isBefore(dateToExcelFinishMonth.toLocalDate())
                                && details.isMissed())
                        .toList();

                Duration totalWorkDurationForRange = Duration.ZERO;
                for (UserWorkDetails details : userWorkDetailsForRange) {
                    totalWorkDurationForRange = totalWorkDurationForRange.plus(Duration.between(details.getStartDateTime(), details.getFinishDateTime()));
                }
                float totalWorkHoursForRange = (float) totalWorkDurationForRange.toMinutes() / 60;
                dataRow.createCell(dates.size() + 1).setCellValue(String.format("%.2f", totalWorkHoursForRange));


                rowIndex++;
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Can't write data to Excel", e);
        } finally {
            workbook.close();
            out.close();
        }
    }

    @Override
    public ByteArrayInputStream dataToUserExcel(List<UserWorkDetails> list) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            Sheet sheet = workbook.createSheet(ALL_EXCEL_NAME);

            // Создайте строку для заголовка
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Працівники");

            LocalDate localDate = list.get(0).getStartDateTime().toLocalDate();
            LocalDateTime dateToExcelStartMonth = LocalDate.of(localDate.getYear(),
                    localDate.getMonth(), 1).atTime(0, 0);
            LocalDateTime dateToExcelFinishMonth = LocalDate.of(localDate.getYear(),
                    localDate.getMonth(),
                    localDate.getMonth().length(localDate.isLeapYear())).atTime(23, 59, 59);

            // Получите список уникальных дат
            List<LocalDate> dates = new ArrayList<>();
            LocalDate currentDate = dateToExcelStartMonth.toLocalDate();

                while (!currentDate.isAfter(dateToExcelFinishMonth.toLocalDate())) {
                dates.add(currentDate);
                currentDate = currentDate.plusDays(1);
            }
            // Создайте заголовки для дат
            int i;
            for (i = 0; i < dates.size(); i++) {
                headerRow.createCell(i + 1).setCellValue(dates.get(i).toString());
            }
            headerRow.createCell(i + 1).setCellValue("Разом");
            // Создайте множество для хранения уникальных пользователей
            Set<User> uniqueUsers = new HashSet<>();
            uniqueUsers.add(list.get(0).getUser());

            int rowIndex = 1;
            for (User user : uniqueUsers) {
                Row dataRow = sheet.createRow(rowIndex);
                dataRow.createCell(0).setCellValue(user.getFirstName() + " " + user.getLastName());

                // Наполните ячейки данными о времени работы
                for (int j = 0; j < dates.size(); j++) {
                    LocalDate date = dates.get(j);
                    List<UserWorkDetails> userWorkDetailsForDate = list.stream()
                            .filter(details -> details.getUser().getId().equals(user.getId())
                                    && details.getStartDateTime().toLocalDate().equals(date)
                                    && !details.isMissed())
                            .toList();

                    // Вычислите суммарное время работы на эту дату
                    Duration totalWorkDuration = Duration.ZERO;
                    for (UserWorkDetails details : userWorkDetailsForDate) {
                        totalWorkDuration = totalWorkDuration.plus(Duration.between(details.getStartDateTime(), details.getFinishDateTime()));
                    }
                    // Форматируйте длительность в формат HH:mm
                    String totalWorkHours = totalWorkDuration.isZero() ? "-" : String.format("%02d:%02d",
                            totalWorkDuration.toHours(),
                            totalWorkDuration.toMinutesPart());

                    dataRow.createCell(j + 1).setCellValue(totalWorkHours);
                }

// Добавляем столбец "Разом"
                List<UserWorkDetails> userWorkDetailsForRange = list.stream()
                        .filter(details -> details.getUser().getId().equals(user.getId())
                                && details.getStartDateTime().toLocalDate().isAfter(dateToExcelStartMonth.toLocalDate())
                                && details.getStartDateTime().toLocalDate().isBefore(dateToExcelFinishMonth.toLocalDate())
                                && details.isMissed())
                        .toList();

                Duration totalWorkDurationForRange = Duration.ZERO;
                for (UserWorkDetails details : userWorkDetailsForRange) {
                    totalWorkDurationForRange = totalWorkDurationForRange.plus(Duration.between(details.getStartDateTime(), details.getFinishDateTime()));
                }
                float totalWorkHoursForRange = (float) totalWorkDurationForRange.toMinutes() / 60;
                dataRow.createCell(dates.size() + 1).setCellValue(String.format("%.2f", totalWorkHoursForRange));


                rowIndex++;
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Can't write data to Excel", e);
        } finally {
            workbook.close();
            out.close();
        }
    }

    @Override
    public ByteArrayInputStream dataFromObjectExcel(List<UserWorkDetails> list, Objects objects) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            Sheet sheet = workbook.createSheet(ALL_EXCEL_NAME);

            // Создайте строку для заголовка
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Працівники");

            LocalDate localDate = LocalDate.now();
            LocalDateTime dateToExcelStartMonth = LocalDate.of(localDate.getYear(),
                    localDate.getMonth(), 1).atTime(0, 0);
            LocalDateTime dateToExcelFinishMonth = LocalDate.of(localDate.getYear(),
                    localDate.getMonth(),
                    localDate.getMonth().length(localDate.isLeapYear())).atTime(23, 59, 59);

            // Получите список уникальных дат
            List<LocalDate> dates = new ArrayList<>();
            LocalDate currentDate = dateToExcelStartMonth.toLocalDate();

            while (!currentDate.isAfter(dateToExcelFinishMonth.toLocalDate())) {
                dates.add(currentDate);
                currentDate = currentDate.plusDays(1);
            }

            // Создайте заголовки для дат
            int i;
            for (i = 0; i < dates.size(); i++) {
                headerRow.createCell(i + 1).setCellValue(dates.get(i).toString());
            }

            headerRow.createCell(i + 1).setCellValue("Разом");

            // Создайте множество для хранения уникальных пользователей

            List<User> users = userService.getAll();

            Set<User> uniqueUsers = new HashSet<>(users);

            int rowIndex = 1;
            for (User user : uniqueUsers) {
                Row dataRow = sheet.createRow(rowIndex);
                dataRow.createCell(0).setCellValue(user.getFirstName() + " " + user.getLastName());

                // Наполните ячейки данными о времени работы
                for (int j = 0; j < dates.size(); j++) {
                    LocalDate date = dates.get(j);
                    List<UserWorkDetails> userWorkDetailsForDate = list.stream()
                            .filter(details -> details.getUser().getId().equals(user.getId())
                                    && details.getStartDateTime().toLocalDate().equals(date)
                                    && !details.isMissed())
                            .toList();

                    // Вычислите суммарное время работы на эту дату
                    Duration totalWorkDuration = Duration.ZERO;
                    for (UserWorkDetails details : userWorkDetailsForDate) {
                        totalWorkDuration = totalWorkDuration.plus(Duration.between(details.getStartDateTime(), details.getFinishDateTime()));
                    }
                    // Форматируйте длительность в формат HH:mm
                    String totalWorkHours = totalWorkDuration.isZero() ? "-" : String.format("%02d:%02d",
                            totalWorkDuration.toHours(),
                            totalWorkDuration.toMinutesPart());

                    dataRow.createCell(j + 1).setCellValue(totalWorkHours);
                }

// Добавляем столбец "Разом"
                List<UserWorkDetails> userWorkDetailsForRange = list.stream()
                        .filter(details -> details.getUser().getId().equals(user.getId())
                                && details.getStartDateTime().toLocalDate().isAfter(dateToExcelStartMonth.toLocalDate())
                                && details.getStartDateTime().toLocalDate().isBefore(dateToExcelFinishMonth.toLocalDate())
                                && !details.isMissed())
                        .toList();

                Duration totalWorkDurationForRange = Duration.ZERO;
                for (UserWorkDetails details : userWorkDetailsForRange) {
                    totalWorkDurationForRange = totalWorkDurationForRange.plus(Duration.between(details.getStartDateTime(), details.getFinishDateTime()));
                }
                float totalWorkHoursForRange = (float) totalWorkDurationForRange.toMinutes() / 60;
                dataRow.createCell(dates.size() + 1).setCellValue(String.format("%.2f", totalWorkHoursForRange));


                rowIndex++;
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Can't write data to Excel", e);
        } finally {
            workbook.close();
            out.close();
        }
    }
}
