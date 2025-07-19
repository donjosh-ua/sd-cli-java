package distributed.systems.sd_cli_java.service;

import java.util.regex.Pattern;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import distributed.systems.sd_cli_java.mapper.UserMapper;
import distributed.systems.sd_cli_java.model.dto.user.UserRequestDTO;
import distributed.systems.sd_cli_java.model.dto.user.UserResponseDTO;
import distributed.systems.sd_cli_java.model.entity.User;
import distributed.systems.sd_cli_java.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");

    public Page<User> getAllUsers(int limit, int offset) {

        if (limit > 100)
            limit = 100;
        if (limit < 1)
            limit = 10;
        if (offset < 0)
            offset = 0;

        Pageable pageable = PageRequest.of(offset / limit, limit);

        return userRepository.findAll(pageable);
    }

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO userDto) {

        validateInput(userDto.getEmail(), userDto.getNickname(), userDto.getPhotoUrl());

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("User already exists with email: " + userDto.getEmail());
        }

        String normalizedEmail = userDto.getEmail().toLowerCase().trim();

        userDto.setEmail(normalizedEmail);

        User savedUser = userRepository.save(userMapper.toEntity(userDto));
        log.info("Created user: {}", savedUser.getEmail());

        return userMapper.toResponseDTO(savedUser);
    }

    @Transactional
    public UserResponseDTO updateUser(UserRequestDTO userDto) {

        validateInput(userDto.getEmail(), userDto.getNickname(), userDto.getPhotoUrl());

        User user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + userDto.getEmail()));

        if (userDto.getNickname() != null)
            user.setNickname(userDto.getNickname().trim());

        if (userDto.getPhotoUrl() != null)
            user.setPhotoUrl(userDto.getPhotoUrl());

        User savedUser = userRepository.save(user);
        log.info("Updated user: {}", savedUser.getEmail());

        return userMapper.toResponseDTO(savedUser);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase().trim())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }

    public Page<User> searchUsersByNickname(String query, int limit, int offset) {

        if (query == null || query.trim().length() < 2) {
            throw new IllegalArgumentException("Search query must be at least 2 characters");
        }

        if (limit > 50)
            limit = 50;
        if (limit < 1)
            limit = 10;
        if (offset < 0)
            offset = 0;

        Pageable pageable = PageRequest.of(offset / limit, limit);
        return userRepository.findByNicknameContainingIgnoreCase(query.trim(), pageable);
    }

    @Transactional
    public boolean deleteUser(String email) {

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required for deletion");
        }

        if (!userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("User not found with email: " + email);
        }

        userRepository.deleteById(email);
        log.info("Deleted user with email: {}", email);

        return true;
    }

    private void validateInput(String email, String nickname, String photoUrl) {
        validateEmail(email);
        validateNickname(nickname);
        if (photoUrl != null && !photoUrl.trim().isEmpty()) {
            validatePhotoUrl(photoUrl);
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (email.length() > 255) {
            throw new IllegalArgumentException("Email must not exceed 255 characters");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    private void validateNickname(String nickname) {
        if (nickname == null || nickname.trim().isEmpty()) {
            throw new IllegalArgumentException("Nickname is required");
        }
        if (nickname.trim().length() > 100) {
            throw new IllegalArgumentException("Nickname must not exceed 100 characters");
        }
    }

    private void validatePhotoUrl(String photoUrl) {
        if (photoUrl != null && photoUrl.length() > 2048) {
            throw new IllegalArgumentException("Photo URL must not exceed 2048 characters");
        }
    }

}
