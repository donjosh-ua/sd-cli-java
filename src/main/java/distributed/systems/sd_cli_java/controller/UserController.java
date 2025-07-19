package distributed.systems.sd_cli_java.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import distributed.systems.sd_cli_java.mapper.UserMapper;
import distributed.systems.sd_cli_java.model.dto.user.UserListResponseDTO;
import distributed.systems.sd_cli_java.model.dto.user.UserRequestDTO;
import distributed.systems.sd_cli_java.model.dto.user.UserResponseDTO;
import distributed.systems.sd_cli_java.model.dto.user.UserSearchResponseDTO;
import distributed.systems.sd_cli_java.model.entity.User;
import distributed.systems.sd_cli_java.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<Object> getAllUsers(@RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {

        Page<User> userPage = userService.getAllUsers(limit, offset);

        UserListResponseDTO response = UserListResponseDTO.builder()
                .users(userMapper.toResponseDTOList(userPage.getContent()))
                .total(userPage.getTotalElements())
                .limit(limit)
                .offset(offset)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{email}")
    public ResponseEntity<Object> getUserByEmail(@PathVariable String email) {
        email = URLDecoder.decode(email, StandardCharsets.UTF_8);
        User user = userService.findByEmail(email);
        return new ResponseEntity<>(userMapper.toResponseDTO(user), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchUsers(@RequestParam String q,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {

        Page<User> userPage = userService.searchUsersByNickname(q, limit, offset);

        UserSearchResponseDTO response = UserSearchResponseDTO.builder()
                .users(userMapper.toSearchItemDTOList(userPage.getContent()))
                .total(userPage.getTotalElements())
                .limit(limit)
                .offset(offset)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody UserRequestDTO request) {
        UserResponseDTO user = userService.createUser(request);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<Object> updateUser(@RequestBody UserRequestDTO request) {
        UserResponseDTO user = userService.updateUser(request);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Object> deleteUser(@PathVariable String email) {
        email = URLDecoder.decode(email, StandardCharsets.UTF_8);
        return new ResponseEntity<>(userService.deleteUser(email), HttpStatus.NO_CONTENT);
    }

}
