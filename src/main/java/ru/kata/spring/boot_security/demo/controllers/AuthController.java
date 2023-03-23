package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Collections;


@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public AuthController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/login")
    public ModelAndView loginPage() {
        return new ModelAndView( "auth/login");
    }

    @GetMapping("/registration")
    public ModelAndView registrationPage(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return new ModelAndView("auth/registration");
    }
    @PostMapping("/registration")
    public ModelAndView createUser(@ModelAttribute("user") User user) throws Exception {
        if (user.getName() != "" && user.getLastName() != "") {
            user.setRoles(roleRepository.findAllById(Collections.singleton(1L)));
            userService.saveUser(user);
        }
        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/registrationadmin")
    public ModelAndView registrationPageAdmin(Model model) {
        model.addAttribute("user", new User());
        return new ModelAndView("auth/registrationadmin");
    }
    @PostMapping("/registrationadmin")
    public ModelAndView createUserAdmin(@ModelAttribute("user") User user) throws Exception {
        user.setRoles(roleRepository.findAllById(Collections.singleton(2L)));
        userService.saveUser(user);
        return new ModelAndView("redirect:/login");
    }
}

