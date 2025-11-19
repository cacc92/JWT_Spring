package com.duocuc.security_jwt.controllers;

import com.duocuc.security_jwt.models.users.User;
import com.duocuc.security_jwt.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.findAll());
    }

    // Antes del request body se realiza la validación que el body que esta llegando corresponda a la estructura de
    // datos que se encuentra definida en la clase que se esta refiriendo USER
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.save(user));
    }
}
