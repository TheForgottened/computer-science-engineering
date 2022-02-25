package pt.isec.metapd.restapi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.isec.metapd.restapi.security.AuthorizationFilter;

import java.sql.SQLException;
import java.util.UnknownFormatConversionException;

import static pt.isec.metapd.RestApi.serverRepository;
import static pt.isec.metapd.RestApi.tokenManager;

@RestController
@RequestMapping("profile")
public class ProfileController {
    @PutMapping("new-name")
    public ResponseEntity<String> putNewName(@RequestBody String newName, @RequestHeader("Authorization") String auth) {
        if (newName == null || newName.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        try {
            String username = tokenManager.getUsernameForToken(AuthorizationFilter.getTokenFromAuth(auth));
            serverRepository.tryToChangeName(username, newName);
        } catch (UnknownFormatConversionException e) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
