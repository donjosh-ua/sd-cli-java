package distributed.systems.sd_cli_java.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import distributed.systems.sd_cli_java.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE LOWER(u.nickname) LIKE LOWER(CONCAT('%', :query, '%')) ORDER BY u.nickname ASC")
    Page<User> findByNicknameContainingIgnoreCase(@Param("query") String query, Pageable pageable);

}
