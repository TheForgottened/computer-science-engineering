package pt.isec.metapd.restapi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.isec.metapd.communication.TinyMessageReceived;
import pt.isec.metapd.communication.TinyUser;
import pt.isec.metapd.restapi.security.AuthorizationFilter;

import java.sql.SQLException;
import java.util.List;
import java.util.UnknownFormatConversionException;

import static pt.isec.metapd.RestApi.serverRepository;
import static pt.isec.metapd.RestApi.tokenManager;

@RestController
@RequestMapping("contacts")
public class ContactsController {
    @GetMapping("list")
    public ResponseEntity<List<TinyUser>> getContactList(@RequestHeader("Authorization") String auth) {
        List<TinyUser> contactList;

        try {
            String username = tokenManager.getUsernameForToken(AuthorizationFilter.getTokenFromAuth(auth));
            contactList = serverRepository.getContactsForUser(username);
        } catch (UnknownFormatConversionException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(contactList);
    }

    @DeleteMapping("remove")
    public ResponseEntity<String> removeContact(@RequestBody String contactUsername, @RequestHeader("Authorization") String auth) {
        if (contactUsername == null || contactUsername.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        try {
            String username = tokenManager.getUsernameForToken(AuthorizationFilter.getTokenFromAuth(auth));

            if (!serverRepository.tryToDeleteContact(username, contactUsername)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (UnknownFormatConversionException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("all-messages")
    public ResponseEntity<List<TinyMessageReceived>> getAllContactMessages(@RequestHeader("Authorization") String auth) {
        List<TinyMessageReceived> messagesReceived;

        try {
            String username = tokenManager.getUsernameForToken(AuthorizationFilter.getTokenFromAuth(auth));
            messagesReceived = serverRepository.getContactMessagesForUser(username);
        } catch (UnknownFormatConversionException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(messagesReceived);
    }

    @GetMapping("messages")
    public ResponseEntity<List<TinyMessageReceived>> getContactMessages(
            @RequestParam String contactUsername,
            @RequestHeader("Authorization") String auth
    ) {
        if (contactUsername == null || contactUsername.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        try {
            String username = tokenManager.getUsernameForToken(AuthorizationFilter.getTokenFromAuth(auth));
            List<TinyUser> contactList = serverRepository.getContactsForUser(username);

            for (TinyUser tinyUser : contactList) {
                if (tinyUser.username().equals(contactUsername)) {
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(serverRepository.getMessagesFromContactForUser(username, contactUsername));
                }
            }
        } catch (UnknownFormatConversionException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
}
