package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;


@Controller
@RequestMapping("/admin")

public class AdminController {
    private final UserService userService;
    private final RoleRepository roleRepository;

    public AdminController(UserService userService,
                           RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }


    @GetMapping()
    public ModelAndView allUsers(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("users", userService.listUsers());
        model.addAttribute("user", user);
        model.addAttribute("allRoles", userService.listRoles());
        return new ModelAndView("admin/admin");
    }

    @GetMapping("/new")
    public ModelAndView createUserForm(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", new User());
        model.addAttribute("us", user);
        model.addAttribute("allRoles", userService.listRoles());
        return new ModelAndView("admin/new");
    }

    @PostMapping(value = "/new")
    public ModelAndView postAddUser(@ModelAttribute("user") User user, @RequestParam("rolesSelected")Long[] rolesId,
                              BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("admin/new");
        }
        List<Role> roles = new ArrayList<>();
        for (Long roleId : rolesId) {
            roles.add(roleRepository.getById(roleId));
        }

        user.setRoles(roles);
        userService.saveUser(user);
        return new ModelAndView("redirect:/admin");
    }

    @DeleteMapping("/{id}/delete")
    public ModelAndView deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return new ModelAndView("redirect:/admin");
    }

    @GetMapping("/{id}/edit")
    public ModelAndView editUserForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.getUser(id)); // добавил
        model.addAttribute("allRoles", userService.listRoles());
        return new ModelAndView("admin/edit");
    }

    //    @PatchMapping("/{id}/edit")
//    public ModelAndView editUser(@ModelAttribute("user") User user, @PathVariable("id") Long id, Model model) {
//        model.addAttribute("allRoles", userService.listRoles());
//        userService.updateUser(user);
//        return new ModelAndView("redirect:/admin");
//    }
    @PatchMapping(value = "/{id}/edit")
    public ModelAndView editUse(@ModelAttribute("user") User user, @RequestParam("rolesSelected")Long[] rolesId,
                          BindingResult bindingResult, Model model){
        model.addAttribute("allRoles", userService.listRoles());
        if (bindingResult.hasErrors()) {
            return new ModelAndView("admin/admin");
        }
        List<Role> roles = new ArrayList<>();
        for (Long roleId : rolesId) {
            roles.add(roleRepository.getById(roleId));
        }

        user.setRoles(roles);
        userService.updateUser(user);
        return new ModelAndView("redirect:/admin");
    }

    @GetMapping(value = "/user")
    public ModelAndView userPage(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);

        return new ModelAndView("admin/adminuser");
    }
}

