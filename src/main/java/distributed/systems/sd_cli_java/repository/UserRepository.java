package distributed.systems.sd_cli_java.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import distributed.systems.sd_cli_java.model.entity.Plan;
import distributed.systems.sd_cli_java.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u JOIN u.plans p WHERE p = ?1")
    List<User> findByPlan(Plan plan);

    @Query("SELECT DISTINCT u FROM User u JOIN u.expenses e WHERE u NOT IN (SELECT d.borrower FROM Debt d)")
    List<User> findUsersWithExpensesAndNoDebts();

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

}
