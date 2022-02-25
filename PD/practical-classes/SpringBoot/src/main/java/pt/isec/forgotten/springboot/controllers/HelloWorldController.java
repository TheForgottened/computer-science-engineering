package pt.isec.forgotten.springboot.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.isec.forgotten.springboot.Nozes;

@RestController
@RequestMapping("hello-world")
public class HelloWorldController {
    @GetMapping()
    public ResponseEntity<String> defaultGet() {
        return ResponseEntity.ok("Bom dia, galera!");
    }

    @GetMapping("{lang}")
    public ResponseEntity<String> get(@PathVariable(required = false) String lang, @RequestHeader("Authorization") String auth) {
        return switch (lang.toUpperCase()) {
            case "PT" -> ResponseEntity.ok("Olá Mundo!");
            case "FR" -> ResponseEntity.ok("Salut tout le Monde!");

            default -> ResponseEntity.status(HttpStatus.OK).body("Hello World!");
        };
    }

    @GetMapping("/nozes")
    public ResponseEntity<Nozes> getNozes() {
        return ResponseEntity.ok(new Nozes("Estas", 2));
    }
}
