package distributed.systems.sd_cli_java.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import distributed.systems.sd_cli_java.model.entity.Plan;
import distributed.systems.sd_cli_java.model.entity.User;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    @Query("SELECT COUNT(p) FROM Plan p JOIN p.users u WHERE u = :user")
    Long countPlansByUser(@Param("user") User user);

    Optional<Plan> findByName(String name);

    @Query("SELECT p FROM Plan p JOIN p.users u WHERE u = :user")
    List<Plan> findByUser(@Param("user") User user);

    List<Plan> findByDateAfter(LocalDateTime date);

}
