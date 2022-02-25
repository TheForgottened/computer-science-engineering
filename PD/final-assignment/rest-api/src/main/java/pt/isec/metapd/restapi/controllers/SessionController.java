package pt.isec.metapd.restapi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pt.isec.metapd.restapi.data.User;

import java.sql.SQLException;

import static pt.isec.metapd.RestApi.serverRepository;
import static pt.isec.metapd.RestApi.tokenManager;

@RestController
public class SessionController {
    @PostMapping("session")
    public ResponseEntity<String> login(@RequestBody() User user) {
        if (user == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        try {
            if (!serverRepository.checkLoginCredentials(user.username(), user.md5Password())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        return ResponseEntity.ok(tokenManager.generateToken(user));
    }
}
