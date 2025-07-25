package distributed.systems.sd_cli_java.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import distributed.systems.sd_cli_java.model.entity.Plan;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    @Query("SELECT p FROM Plan p JOIN p.participants u WHERE u.email = :participantId")
    List<Plan> findByParticipantId(String participantId);

    Optional<Plan> findByCode(String code);

}