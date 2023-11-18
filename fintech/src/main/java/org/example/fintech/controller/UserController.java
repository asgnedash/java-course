package org.example.fintech.controller;

import org.example.fintech.entities.User;
import org.example.fintech.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Registration")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201"
            ),
            @ApiResponse(
                    responseCode = "400"
            )
    })
    @PostMapping("/user/register")
    public ResponseEntity<?> createUser(@Validated @RequestParam String username, @RequestParam String password) {
        User user = userService.create(username, password);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
