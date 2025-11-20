package com.duocuc.security_jwt.controllers.users;

import com.duocuc.security_jwt.models.users.User;
import com.duocuc.security_jwt.services.users.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
// CORS para cualquier ruta, lo ideal es delimitar de donde vendra el trafico, como es la parte de origins
@CrossOrigin(originPatterns = "*", origins = "http://localhost:5300")
@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    // @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.findAll());
    }

    // @PreAuthorize("hasRole('ADMIN')")
    // Antes del request body se realiza la validación que el body que esta llegando corresponda a la estructura de
    // datos que se encuentra definida en la clase que se esta refiriendo USER
    // Esta clase me va a permitir crear usuarios admin
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.save(user));
    }

    // Este método nos permite generar el registro de un usuario normal
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
        user.setAdmin(false);
        return this.createUser(user);
    }

}
