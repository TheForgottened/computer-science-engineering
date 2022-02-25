package pt.isec.forgotten.springboot.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pt.isec.forgotten.springboot.data.User;

@RestController
public class SessionController {
    @PostMapping("session")
    public ResponseEntity<String> login(@RequestBody(required = false) User user) {
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed!");
        System.out.println(user);
        String token = user.getUsername() + "_123";
        return ResponseEntity.ok(token);
    }
}
