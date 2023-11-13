package app.equalityboot.service.impl;

import app.equalityboot.model.*;
import app.equalityboot.service.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
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
            headerRow.createCell(0).setCellValue("Imię i Nazwisko");

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

            headerRow.createCell(i + 1).setCellValue("Suma");

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

// Добавляем столбец "Suma"
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
            headerRow.createCell(0).setCellValue("Imię i Nazwisko");

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

            headerRow.createCell(i + 1).setCellValue("Suma");

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

// Добавляем столбец "Suma"
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
            headerRow.createCell(0).setCellValue("Imię i Nazwisko");

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
            headerRow.createCell(i + 1).setCellValue("Suma");
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

// Добавляем столбец "Suma"
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
    public ByteArrayInputStream dataFromObjectExcelMonth(List<UserWorkDetails> list, Objects objects) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            Sheet sheet = workbook.createSheet(ALL_EXCEL_NAME);

            // Создайте строку для заголовка
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Imię i Nazwisko");

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

            headerRow.createCell(i + 1).setCellValue("Suma");

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

// Добавляем столбец "Suma"
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

    @Override
    public ByteArrayInputStream dataFromObjectExcelWeek(List<UserWorkDetails> list, Objects objects, LocalDate startDate) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            Sheet sheet = workbook.createSheet(ALL_EXCEL_NAME);

            // Создайте строку для заголовка
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Imię i Nazwisko");

            LocalDate dateToExcelFinishWeek = startDate.plusDays(7);

            // Получите список уникальных дат
            List<LocalDate> dates = new ArrayList<>();

            while (!startDate.isAfter(dateToExcelFinishWeek)) {
                dates.add(startDate);
                startDate = startDate.plusDays(1);
            }

            // Создайте заголовки для дат
            int i;
            for (i = 0; i < dates.size(); i++) {
                headerRow.createCell(i + 1).setCellValue(dates.get(i).toString());
            }

            headerRow.createCell(i + 1).setCellValue("Suma");

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

// Добавляем столбец "Suma"
                LocalDate finalStartDate = startDate;
                List<UserWorkDetails> userWorkDetailsForRange = list.stream()
                        .filter(details -> details.getUser().getId().equals(user.getId())
                                && details.getStartDateTime().toLocalDate().isAfter(finalStartDate)
                                && details.getStartDateTime().toLocalDate().isBefore(dateToExcelFinishWeek)
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

    @Override
    public ByteArrayInputStream dataForManager(Order order) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            Sheet sheet = workbook.createSheet(ALL_EXCEL_NAME);
            addAdditionalHeaders(sheet, order);
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Can't write data to Excel", e);
        } finally {
            workbook.close();
            out.close();
        }
    }

    private void addAdditionalHeaders(Sheet sheet, Order order) {
        List<OrderUser> byOrder = orderUserService.getByOrder(order);
        List<User> users = byOrder.stream()
                .map(OrderUser::getUser)
                .toList();

        // Merge cells A1 to F1 for "Quality Work (for managers)"
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 6));
        Row qualityWorkRow = sheet.createRow(0);
        Cell qualityWorkCell = qualityWorkRow.createCell(0);
        qualityWorkCell.setCellValue("Quality Work (for managers)                                               Date:"
                +"\n" + "                                                                                     " + order.getDate());

        // Set "Imię Nazwisko" in A3
        Row imieNazwiskoRow = sheet.createRow(2);
        Cell imieNazwiskoCell = imieNazwiskoRow.createCell(0);
        imieNazwiskoCell.setCellValue("Imię \n"+" Nazwisko");
        sheet.addMergedRegion(new CellRangeAddress(2, 3, 0, 0));

        int rowIndex = 4;
        for (User user : users) {
            if(user != null) {
                Row dataRow = sheet.createRow(rowIndex);
                dataRow.createCell(0).setCellValue(user.getFirstName() + " " + user.getLastName());
                rowIndex++;
            }
        }

        // Set "G 16-00" in B3 and merge B3:C3
        Cell g1600Cell = imieNazwiskoRow.createCell(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        g1600Cell.setCellValue(order.getStartTime().format(formatter));
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 2));

        // Set "Rozp." and "Zakoń." in B4 and C4
        Row rozpZakonRow = sheet.createRow(3);
        Cell rozpCell = rozpZakonRow.createCell(1);
        Cell zakonCell = rozpZakonRow.createCell(2);
        rozpCell.setCellValue("Rozp.");
        zakonCell.setCellValue("Zakoń.");

        // Set "Łączna ilość god." in D3 and merge D3:D4
        Cell lacznaIloscGodCell = imieNazwiskoRow.createCell(3);
        lacznaIloscGodCell.setCellValue("Łączna \n" + " ilość god.");
        sheet.addMergedRegion(new CellRangeAddress(2, 3, 3, 3));

        // Set "Podpis pracownika" in E3 and merge E3:E4
        Cell podpisPracownikaCell = imieNazwiskoRow.createCell(4);
        podpisPracownikaCell.setCellValue("Podpis \n" + " pracownika");
        sheet.addMergedRegion(new CellRangeAddress(2, 3, 4, 4));

        // Set "Zaangażowania w pracy + lub -" in F3 and merge F3:F4
        Cell zaangazowaniaCell = imieNazwiskoRow.createCell(5);
        zaangazowaniaCell.setCellValue("Zaangażowania \n" + " w pracy + lub -");
        sheet.addMergedRegion(new CellRangeAddress(2, 3, 5, 5));

        // Set "Podpis menedżera" in G3 and merge G3:G4
        Cell podpisMenedzeraCell = imieNazwiskoRow.createCell(6);
        podpisMenedzeraCell.setCellValue("Podpis \n" + " menedżera");
        sheet.addMergedRegion(new CellRangeAddress(2, 3, 6, 6));
    }
}
