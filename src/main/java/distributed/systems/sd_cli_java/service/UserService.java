package distributed.systems.sd_cli_java.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import distributed.systems.sd_cli_java.model.entity.Plan;
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

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public List<User> findUsersByPlan(Plan plan) {
        return userRepository.findByPlan(plan);
    }

    public List<User> findUsersWithExpensesAndNoDebts() {
        return userRepository.findUsersWithExpensesAndNoDebts();
    }

    @Transactional
    public User findOrCreateByUsername(String username) {
        Optional<User> existingUser = findByUsername(username);
        if (existingUser.isPresent()) {
            return existingUser.get();
        } else {
            log.info("Creating new user with username: {}", username);
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword("default-password"); // You might want to set a secure default or require password later
            return createUser(newUser);
        }
    }
}
