package distributed.systems.sd_cli_java.service;

import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");

    // New API methods

    /**
     * Create or update user based on email
     */
    public User createOrUpdateUser(String email, String nickname, String photoUrl) {
        validateInput(email, nickname, photoUrl);

        String normalizedEmail = email.toLowerCase().trim();
        String trimmedNickname = nickname.trim();

        Optional<User> existingUser = userRepository.findByEmail(normalizedEmail);

        if (existingUser.isPresent()) {
            // Update existing user
            User user = existingUser.get();
            user.setNickname(trimmedNickname);
            user.setPhotoUrl(photoUrl);
            log.info("Updating user: {}", normalizedEmail);
            return userRepository.save(user);
        } else {
            // Create new user
            User newUser = User.builder()
                    .email(normalizedEmail)
                    .nickname(trimmedNickname)
                    .photoUrl(photoUrl)
                    .build();
            log.info("Creating new user: {}", normalizedEmail);
            return userRepository.save(newUser);
        }
    }

    /**
     * Find user by email
     */
    public Optional<User> findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return Optional.empty();
        }
        return userRepository.findByEmail(email.toLowerCase().trim());
    }

    /**
     * Update user profile
     */
    public Optional<User> updateUserProfile(String email, String nickname, String photoUrl) {
        validateNickname(nickname);
        if (photoUrl != null) {
            validatePhotoUrl(photoUrl);
        }

        return userRepository.findByEmail(email.toLowerCase().trim())
                .map(user -> {
                    if (nickname != null) {
                        user.setNickname(nickname.trim());
                    }
                    if (photoUrl != null) {
                        user.setPhotoUrl(photoUrl);
                    }
                    log.info("Updating profile for user: {}", email);
                    return userRepository.save(user);
                });
    }

    /**
     * Search users by nickname with pagination
     */
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

    /**
     * Get all users with pagination
     */
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

    /**
     * Delete user by email
     */
    public boolean deleteUser(String email) {
        String normalizedEmail = email.toLowerCase().trim();
        if (userRepository.existsByEmail(normalizedEmail)) {
            userRepository.deleteById(normalizedEmail);
            log.info("Deleted user: {}", normalizedEmail);
            return true;
        }
        return false;
    }

    /**
     * Check if user exists by email
     */
    public boolean existsByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return userRepository.existsByEmail(email.toLowerCase().trim());
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
        // Add more URL validation if needed
    }
}
