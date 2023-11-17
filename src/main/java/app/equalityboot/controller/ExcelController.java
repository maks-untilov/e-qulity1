package app.equalityboot.controller;

import app.equalityboot.model.*;
import app.equalityboot.service.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller()
@RequestMapping("/excel")
public class ExcelController {
    private final ExcelGeneratorService excelGeneratorService;
    private final UserWorkDetailsService userWorkDetailsService;
    private final UserService userService;
    private final OrderService orderService;
    private final ObjectsService objectsService;
    private final OrderUserService orderUserService;

    public ExcelController(ExcelGeneratorService excelGeneratorService, UserWorkDetailsService userWorkDetailsService,
                           UserService userService, OrderService orderService,
                           ObjectsService objectsService, OrderUserService orderUserService) {
        this.excelGeneratorService = excelGeneratorService;
        this.userWorkDetailsService = userWorkDetailsService;
        this.userService = userService;
        this.orderService = orderService;
        this.objectsService = objectsService;
        this.orderUserService = orderUserService;
    }

    @GetMapping("/all/{date}")
    public ResponseEntity<Resource> getDownloadAll(@PathVariable String date) throws IOException {
        LocalDate localDate = LocalDate.parse(date);
        LocalDateTime dateToExcelStartMonth = LocalDate.of(localDate.getYear(),
                localDate.getMonth(), 1).atTime(0, 0);
        LocalDateTime dateToExcelFinishMonth = LocalDate.of(localDate.getYear(),
                localDate.getMonth(),
                localDate.getMonth().length(localDate.isLeapYear())).atTime(23, 59, 59);
        String fileName = "excel_all.xlsx";
        List<UserWorkDetails> userWorkDetailsList =
                userWorkDetailsService.getAllGreaterThan(dateToExcelStartMonth, dateToExcelFinishMonth);
        ByteArrayInputStream actualData = excelGeneratorService.dataToExcel(userWorkDetailsList);
        InputStreamResource file = new InputStreamResource(actualData);
        ResponseEntity<Resource> body = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
        return body;
    }

    @GetMapping("/all/byCoordinator/{date}/{coordinatorId}")
    public ResponseEntity<Resource> getDownloadAllByCoordinator(@PathVariable String date,
                                                                @PathVariable String coordinatorId) throws IOException {
        LocalDate localDate = LocalDate.parse(date);
        User coordinator = userService.get(Long.parseLong(coordinatorId));
        LocalDateTime dateToExcelStartMonth = LocalDate.of(localDate.getYear(),
                localDate.getMonth(), 1).atTime(0, 0);
        LocalDateTime dateToExcelFinishMonth = LocalDate.of(localDate.getYear(),
                localDate.getMonth(),
                localDate.getMonth().length(localDate.isLeapYear())).atTime(23, 59, 59);
        String fileName = "excel_all_by_" + coordinator.getFirstName() + " " + coordinator.getLastName() + ".xlsx";
        List<UserWorkDetails> userWorkDetailsList =
                userWorkDetailsService.getAllGreaterThan(dateToExcelStartMonth, dateToExcelFinishMonth);
        ByteArrayInputStream actualData = excelGeneratorService.dataToExcelByCoordinator(userWorkDetailsList, coordinator);
        InputStreamResource file = new InputStreamResource(actualData);
        ResponseEntity<Resource> body = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
        return body;
    }

    @GetMapping("/all/passedDays/{date}")
    public ResponseEntity<Resource> getDownloadAllPassedDays(@PathVariable String date) throws IOException {
        LocalDate localDate = LocalDate.parse(date);
        LocalDateTime dateToExcelStartMonth = LocalDate.of(localDate.getYear(),
                localDate.getMonth(), 1).atTime(0, 0);
        LocalDateTime dateToExcelFinishMonth = LocalDate.of(localDate.getYear(),
                localDate.getMonth(),
                localDate.getMonth().length(localDate.isLeapYear())).atTime(23, 59, 59);
        String fileName = "excel_all_passed.xlsx";
        List<UserWorkDetails> userWorkDetailsList =
                userWorkDetailsService.getAllGreaterThan(dateToExcelStartMonth,
                        dateToExcelFinishMonth);
        ByteArrayInputStream actualData = excelGeneratorService.dataToExcelPassedDays(userWorkDetailsList);
        InputStreamResource file = new InputStreamResource(actualData);
        ResponseEntity<Resource> body = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
        return body;
    }

    @GetMapping("/object/byCoordinator/{objectId}/{date}")
    public ResponseEntity<Resource> getDownloadObjectMonthForCoordinator(@PathVariable String date, @PathVariable String objectId,
                                                                         @RequestParam String coordinatorId) throws IOException {
        User coordinator = userService.get(Long.parseLong(coordinatorId));
        LocalDate localDate = LocalDate.parse(date);
        Objects objects = objectsService.get(Long.parseLong(objectId));
        LocalDateTime dateToExcelStartMonth = LocalDate.of(localDate.getYear(),
                localDate.getMonth(), 1).atTime(0, 0);
        LocalDateTime dateToExcelFinishMonth = LocalDate.of(localDate.getYear(),
                localDate.getMonth(),
                localDate.getMonth().length(localDate.isLeapYear())).atTime(23, 59, 59);
        List<Order> orders = orderService.getOrderByDateBetween(dateToExcelStartMonth.toLocalDate(),
                        dateToExcelFinishMonth.toLocalDate()).stream()
                .filter(order -> order.getObject().equals(objects))
                .toList();
        List<UserWorkDetails> dataToExcel = new ArrayList<>();
        for (Order order : orders) {
            dataToExcel = (userWorkDetailsService.getUserWorkDetailsByOrderAndDate(order,
                    orders.get(0).getStartTime(),
                    orders.get(orders.size() - 1).getFinishTime()));
        }
        String fileName = "excel_" + objects.getName() + ".xlsx";
        ByteArrayInputStream actualData = excelGeneratorService.dataFromObjectExcelMonthByCoordinator(dataToExcel,
                objects, coordinator);
        InputStreamResource file = new InputStreamResource(actualData);
        ResponseEntity<Resource> body = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
        return body;
    }

    @GetMapping("/{userId}/{date}")
    public ResponseEntity<Resource> getDownloadUser(@PathVariable String date, @PathVariable String userId) throws IOException {
        User user = userService.get(Long.parseLong(userId));
        LocalDate localDate = LocalDate.parse(date);
        LocalDateTime dateToExcelStartMonth = LocalDate.of(localDate.getYear(),
                localDate.getMonth(), 1).atTime(0, 0);
        LocalDateTime dateToExcelFinishMonth = LocalDate.of(localDate.getYear(),
                localDate.getMonth(),
                localDate.getMonth().length(localDate.isLeapYear())).atTime(23, 59, 59);
        String fileName = "excel_user" + userId + ".xlsx";
        List<UserWorkDetails> userWorkDetails =
                userWorkDetailsService.getListByUserBetweenTime(user, dateToExcelStartMonth,
                        dateToExcelFinishMonth);
        ByteArrayInputStream actualData = excelGeneratorService.dataToUserExcel(userWorkDetails);
        InputStreamResource file = new InputStreamResource(actualData);
        ResponseEntity<Resource> body = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
        return body;
    }

    @GetMapping("/object/month/{objectId}/{date}")
    public ResponseEntity<Resource> getDownloadObjectMonth(@PathVariable String date, @PathVariable String objectId) throws IOException {
        LocalDate localDate = LocalDate.parse(date);
        Objects objects = objectsService.get(Long.parseLong(objectId));
        LocalDateTime dateToExcelStartMonth = LocalDate.of(localDate.getYear(),
                localDate.getMonth(), 1).atTime(0, 0);
        LocalDateTime dateToExcelFinishMonth = LocalDate.of(localDate.getYear(),
                localDate.getMonth(),
                localDate.getMonth().length(localDate.isLeapYear())).atTime(23, 59, 59);
        List<Order> orders = orderService.getOrderByDateBetween(dateToExcelStartMonth.toLocalDate(),
                dateToExcelFinishMonth.toLocalDate()).stream()
                .filter(order -> order.getObject().equals(objects))
                .toList();
        List<UserWorkDetails> dataToExcel = new ArrayList<>();
        for (Order order : orders) {
             dataToExcel = (userWorkDetailsService.getUserWorkDetailsByOrderAndDate(order,
                    orders.get(0).getStartTime(),
                    orders.get(orders.size() - 1).getFinishTime()));
        }
        String fileName = "excel_" + objects.getName() + ".xlsx";
        ByteArrayInputStream actualData = excelGeneratorService.dataFromObjectExcelMonth(dataToExcel, objects);
        InputStreamResource file = new InputStreamResource(actualData);
        ResponseEntity<Resource> body = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
        return body;
    }

    @GetMapping("/object/week/{objectId}/{date}")
    public ResponseEntity<Resource> getDownloadObjectWeek(@PathVariable String date, @PathVariable String objectId) throws IOException {
        LocalDate localDate = LocalDate.parse(date);
        Objects objects = objectsService.get(Long.parseLong(objectId));
        LocalDateTime dateToExcelStartMonth = LocalDate.of(localDate.getYear(),
                localDate.getMonth(), 1).atTime(0, 0);
        LocalDateTime dateToExcelFinishMonth = LocalDate.of(localDate.getYear(),
                localDate.getMonth(),
                localDate.getMonth().length(localDate.isLeapYear())).atTime(23, 59, 59);
        List<Order> orders = orderService.getOrderByDateBetween(dateToExcelStartMonth.toLocalDate(),
                        dateToExcelFinishMonth.toLocalDate()).stream()
                .filter(order -> order.getObject().equals(objects))
                .toList();
        List<UserWorkDetails> dataToExcel = new ArrayList<>();
        for (Order order : orders) {
            dataToExcel = (userWorkDetailsService.getUserWorkDetailsByOrderAndDate(order,
                    orders.get(0).getStartTime(),
                    orders.get(orders.size() - 1).getFinishTime()));
        }
        String fileName = "excel_" + objects.getName() + ".xlsx";
        ByteArrayInputStream actualData = excelGeneratorService.dataFromObjectExcelWeek(dataToExcel, objects, localDate);
        InputStreamResource file = new InputStreamResource(actualData);
        ResponseEntity<Resource> body = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
        return body;
    }

    @GetMapping("/object/for/manager/{orderId}")
    public ResponseEntity<Resource> getDownloadForManagers(@PathVariable String orderId) throws IOException {

        Order order = orderService.getById(Long.parseLong(orderId));
        String fileName = "excel_for_managers" + orderId + ".xlsx";
        ByteArrayInputStream actualData = excelGeneratorService.dataForManager(order);
        InputStreamResource file = new InputStreamResource(actualData);
        ResponseEntity<Resource> body = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
        return body;
    }
}
