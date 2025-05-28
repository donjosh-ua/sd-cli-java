package distributed.systems.sd_cli_java.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import distributed.systems.sd_cli_java.model.dto.UserDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        throw new IllegalArgumentException("Not implemented yet");
    }

    @GetMapping("/search-by-id/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        throw new IllegalArgumentException("Not implemented yet");
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserDTO user) {
        throw new IllegalArgumentException("Not implemented yet");
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO user) {
        throw new IllegalArgumentException("Not implemented yet");
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestBody Map<String, Long> fields) {
        throw new IllegalArgumentException("Not implemented yet");
    }

}
