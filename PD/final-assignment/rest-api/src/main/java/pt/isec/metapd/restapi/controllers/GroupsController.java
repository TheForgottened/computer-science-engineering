package pt.isec.metapd.restapi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.isec.metapd.communication.TinyGroup;
import pt.isec.metapd.communication.TinyMessageReceived;
import pt.isec.metapd.restapi.security.AuthorizationFilter;

import java.sql.SQLException;
import java.util.List;
import java.util.UnknownFormatConversionException;

import static pt.isec.metapd.RestApi.serverRepository;
import static pt.isec.metapd.RestApi.tokenManager;

@RestController
@RequestMapping("groups")
public class GroupsController {
    @GetMapping("list")
    public ResponseEntity<List<TinyGroup>> getGroups(@RequestHeader("Authorization") String auth) {
        List<TinyGroup> groupList;

        try {
            String username = tokenManager.getUsernameForToken(AuthorizationFilter.getTokenFromAuth(auth));
            groupList = serverRepository.getGroupsForUser(username);
        } catch (UnknownFormatConversionException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(groupList);
    }

    @GetMapping("all-messages")
    public ResponseEntity<List<TinyMessageReceived>> getAllGroupMessages(@RequestHeader("Authorization") String auth) {
        List<TinyMessageReceived> messagesReceived;

        try {
            String username = tokenManager.getUsernameForToken(AuthorizationFilter.getTokenFromAuth(auth));
            messagesReceived = serverRepository.getGroupMessagesForUser(username);
        } catch (UnknownFormatConversionException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(messagesReceived);
    }

    @GetMapping("messages")
    public ResponseEntity<List<TinyMessageReceived>> getGroupMessages(
            @RequestParam int groupId,
            @RequestHeader("Authorization") String auth
    ) {
        try {
            String username = tokenManager.getUsernameForToken(AuthorizationFilter.getTokenFromAuth(auth));
            List<TinyGroup> groupList = serverRepository.getGroupsForUser(username);

            for (TinyGroup tinyGroup : groupList) {
                if (tinyGroup.id() == groupId) {
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(serverRepository.getMessagesFromGroup(groupId));
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
