package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Arrays;

@Component
public class UserUpload implements CommandLineRunner {

    private final UserService userService;
    @Autowired
    public UserUpload(UserService userService) {
        this.userService = userService;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Role ROLE_USER = new Role("ROLE_USER");
        Role ROLE_ADMIN = new Role("ROLE_ADMIN");
        userService.saveRoles(ROLE_USER);
        userService.saveRoles(ROLE_ADMIN);
        userService.saveUser(new User("Andrew", "Kim", (byte)24, "admin@mail.ru", "qwe123",
                Arrays.asList(ROLE_ADMIN, ROLE_USER)));
        userService.saveUser(new User("Veronika", "Kim", (byte)25, "user@mail.ru", "qwe123",
                Arrays.asList(ROLE_USER)));
    }
}
