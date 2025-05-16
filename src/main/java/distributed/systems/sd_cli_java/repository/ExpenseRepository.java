package distributed.systems.sd_cli_java.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import distributed.systems.sd_cli_java.model.entity.Expense;
import distributed.systems.sd_cli_java.model.entity.Plan;
import distributed.systems.sd_cli_java.model.entity.User;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUser(User user);

    List<Expense> findByPlan(Plan plan);

    List<Expense> findByType(String type);

    List<Expense> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Expense> findByPlanAndType(Plan plan, String type);

    List<Expense> findByUserAndPlan(User user, Plan plan);

}
