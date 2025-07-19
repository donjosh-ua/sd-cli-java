package distributed.systems.sd_cli_java.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import distributed.systems.sd_cli_java.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    // New API methods using email as primary key
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<User> findByNicknameContainingIgnoreCase(String trim, Pageable pageable);

}
