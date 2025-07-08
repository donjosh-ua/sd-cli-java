package distributed.systems.sd_cli_java.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import distributed.systems.sd_cli_java.mapper.UserMapper;
import distributed.systems.sd_cli_java.model.dto.ErrorResponseDTO;
import distributed.systems.sd_cli_java.model.dto.UserCreateUpdateResponseDTO;
import distributed.systems.sd_cli_java.model.dto.UserListResponseDTO;
import distributed.systems.sd_cli_java.model.dto.UserRequestDTO;
import distributed.systems.sd_cli_java.model.dto.UserSearchResponseDTO;
import distributed.systems.sd_cli_java.model.dto.UserUpdateRequestDTO;
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
        try {
            Page<User> userPage = userService.getAllUsers(limit, offset);
            
            UserListResponseDTO response = UserListResponseDTO.builder()
                    .users(userMapper.toResponseDTOList(userPage.getContent()))
                    .total(userPage.getTotalElements())
                    .limit(limit)
                    .offset(offset)
                    .build();
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            log.error("Error getting all users", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("INTERNAL_ERROR", "Server error", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }
    
    @GetMapping("/{email}")
    public ResponseEntity<Object> getUserByEmail(@PathVariable String email) {
        try {
            String decodedEmail = URLDecoder.decode(email, StandardCharsets.UTF_8);
            Optional<User> user = userService.findByEmail(decodedEmail);
            
            if (user.isPresent()) {
                return new ResponseEntity<>(userMapper.toResponseDTO(user.get()), HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("USER_NOT_FOUND", "User not found", HttpStatus.NOT_FOUND.value()));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse("INVALID_EMAIL", e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        } catch (Exception e) {
            log.error("Error getting user by email", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("INTERNAL_ERROR", "Server error", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchUsers(@RequestParam String q,
                                       @RequestParam(defaultValue = "10") int limit,
                                       @RequestParam(defaultValue = "0") int offset) {
        try {
            if (q == null || q.trim().length() < 2) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(createErrorResponse("INVALID_REQUEST", 
                            "Search query must be at least 2 characters", HttpStatus.BAD_REQUEST.value()));
            }
            
            Page<User> userPage = userService.searchUsersByNickname(q, limit, offset);
            
            UserSearchResponseDTO response = UserSearchResponseDTO.builder()
                    .users(userMapper.toSearchItemDTOList(userPage.getContent()))
                    .total(userPage.getTotalElements())
                    .limit(limit)
                    .offset(offset)
                    .build();
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            log.error("Error searching users", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("INTERNAL_ERROR", "Server error", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @PostMapping
    public ResponseEntity<Object> createOrUpdateUser(@RequestBody UserRequestDTO request) {
        try {
            boolean userExists = userService.existsByEmail(request.getEmail());
            User user = userService.createOrUpdateUser(
                request.getEmail(), 
                request.getNickname(), 
                request.getPhotoUrl()
            );
            
            UserCreateUpdateResponseDTO response = UserCreateUpdateResponseDTO.builder()
                    .success(true)
                    .user(userMapper.toResponseDTO(user))
                    .isNewUser(!userExists)
                    .build();
            
            HttpStatus status = userExists ? HttpStatus.OK : HttpStatus.CREATED;
            return new ResponseEntity<>(response, status);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse("INVALID_REQUEST", e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        } catch (Exception e) {
            log.error("Error creating/updating user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("INTERNAL_ERROR", "Server error", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @PutMapping("/{email}")
    public ResponseEntity<Object> updateUserProfile(@PathVariable String email, 
                                             @RequestBody UserUpdateRequestDTO request) {
        try {
            String decodedEmail = URLDecoder.decode(email, StandardCharsets.UTF_8);
            Optional<User> updatedUser = userService.updateUserProfile(
                decodedEmail, 
                request.getNickname(), 
                request.getPhotoUrl()
            );
            
            if (updatedUser.isPresent()) {
                return new ResponseEntity<>(userMapper.toResponseDTO(updatedUser.get()), HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("USER_NOT_FOUND", "User not found", HttpStatus.NOT_FOUND.value()));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse("INVALID_REQUEST", e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        } catch (Exception e) {
            log.error("Error updating user profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("INTERNAL_ERROR", "Server error", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Object> deleteUser(@PathVariable String email) {
        try {
            String decodedEmail = URLDecoder.decode(email, StandardCharsets.UTF_8);
            boolean deleted = userService.deleteUser(decodedEmail);
            
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("USER_NOT_FOUND", "User not found", HttpStatus.NOT_FOUND.value()));
            }
        } catch (Exception e) {
            log.error("Error deleting user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("INTERNAL_ERROR", "Server error", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    private ErrorResponseDTO createErrorResponse(String errorCode, String message, int statusCode) {
        return ErrorResponseDTO.builder()
                .error(errorCode)
                .message(message)
                .code(statusCode)
                .build();
    }
}
