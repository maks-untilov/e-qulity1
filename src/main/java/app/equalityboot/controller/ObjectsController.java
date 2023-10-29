package app.equalityboot.controller;

import app.equalityboot.dto.mapping.ObjectsMapper;
import app.equalityboot.dto.response.ObjectResponseDto;
import app.equalityboot.model.Objects;
import app.equalityboot.model.User;
import app.equalityboot.service.ObjectsService;
import app.equalityboot.service.impl.ObjectsServiceImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/objects")
public class ObjectsController {
    private ObjectsService objectsService;
    private ObjectsMapper objectsMapper;

    public ObjectsController(ObjectsService objectsService, ObjectsMapper objectsMapper) {
        this.objectsService = objectsService;
        this.objectsMapper = objectsMapper;
    }

    @GetMapping
    public String get(Model model, @AuthenticationPrincipal User user) {
        List<ObjectResponseDto> objects = objectsService.gelAll()
                .stream()
                .map(object -> objectsMapper.toResponseDto(object))
                .collect(Collectors.toList());
        model.addAttribute("objects_list", objects);
        model.addAttribute("user", user);
        return "objects";
    }

    @PostMapping
    public String post(Model model, @RequestParam String title, @RequestParam String address,
                       @RequestParam String description, @AuthenticationPrincipal User user) {
        Objects objects = new Objects();
        objects.setName(title);
        objects.setAddress(address);
        objects.setDescription(description);
        objectsService.add(objects);
        List<ObjectResponseDto> objectsList = objectsService.gelAll()
                .stream()
                .map(object -> objectsMapper.toResponseDto(object))
                .collect(Collectors.toList());
        model.addAttribute("objects_list", objectsList);
        model.addAttribute("user", user);
        return "objects";
    }

    @GetMapping("/edit/{objId}")
    public String get(Model model, @PathVariable("objId") String objId, @AuthenticationPrincipal User user) {
        Objects objects = objectsService.get(Long.parseLong(objId));
        model.addAttribute("ob", objects);
        model.addAttribute("loggedUser", user);
        return "editObjects";
    }

    @PostMapping("/edit/{objId}")
    public String post(Model model, @PathVariable("objId") String objId, @RequestParam String title,
                       @RequestParam String address, @RequestParam String description, @RequestParam String phone,
                       @RequestParam String email,@AuthenticationPrincipal User user) {
        Objects objects = objectsService.get(Long.parseLong(objId));
        objects.setName(title);
        objects.setAddress(address);
        objects.setDescription(description);
        objects.setEmail(email);
        objects.setPhoneNumber(phone);
        objectsService.add(objects);
        model.addAttribute("user", user);
        List<ObjectResponseDto> objectsList = objectsService.gelAll()
                .stream()
                .map(object -> objectsMapper.toResponseDto(object))
                .collect(Collectors.toList());
        model.addAttribute("objects_list", objectsList);
        return "objects";
    }
}
