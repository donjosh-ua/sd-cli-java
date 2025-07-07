package distributed.systems.sd_cli_java.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import distributed.systems.sd_cli_java.model.entity.Expense;
import distributed.systems.sd_cli_java.model.entity.Plan;
import distributed.systems.sd_cli_java.model.entity.User;
import distributed.systems.sd_cli_java.repository.ExpenseRepository;
import distributed.systems.sd_cli_java.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PlanService {

    private final PlanRepository planRepository;
    private final ExpenseRepository expenseRepository;
    private final UserService userService;

    public Plan createPlan(Plan plan) {
        return planRepository.save(plan);
    }

    public Plan updatePlan(Plan plan) {
        return planRepository.save(plan);
    }

    public Optional<Plan> findById(Long id) {
        return planRepository.findById(id);
    }

    public List<Plan> findAllPlans() {
        return planRepository.findAll();
    }

    public void deletePlan(Long id) {
        planRepository.deleteById(id);
    }

    public Plan addUserToPlan(Plan plan, User user) {
        plan.getUsers().add(user);
        return planRepository.save(plan);
    }

    public Plan removeUserFromPlan(Plan plan, User user) {
        plan.getUsers().remove(user);
        return planRepository.save(plan);
    }

    public Plan addExpenseToPlan(Plan plan, Expense expense) {
        expense.setPlan(plan);
        expenseRepository.save(expense);
        return planRepository.findById(plan.getPlanId()).orElse(plan);
    }

    public List<Plan> findPlansByDateAfter(LocalDateTime date) {
        return planRepository.findByDateAfter(date);
    }

    public List<Plan> findPlansByUser(User user) {
        return planRepository.findByUser(user);
    }

    public Optional<Plan> findByName(String name) {
        return planRepository.findByName(name);
    }

    public String generateInvitationLink(Long planId) {
        // In a real app, you'd probably have a more sophisticated way to generate and
        // validate links
        return "http://yourapp.com/join/" + planId + "/" + UUID.randomUUID().toString();
    }

    public Long countPlansByUser(User user) {
        return planRepository.countPlansByUser(user);
    }

    // Add this method to PlanService.java
    /**
     * Add a user to a plan by nickname
     * This method finds or creates the user based on nickname and adds them to the
     * plan
     * 
     * @param nickname The nickname of the user to add
     * @param planId   The ID of the plan to add the user to
     * @return true if successful, false otherwise
     */
    @Transactional
    public boolean addUserToPlanByNickname(String nickname, Long planId) {
        try {
            // Find the plan first
            Optional<Plan> planOpt = findById(planId);
            if (planOpt.isEmpty()) {
                log.warn("Plan with ID {} not found", planId);
                return false;
            }

            Plan plan = planOpt.get();

            // Find or create the user
            User user = userService.findByEmail(nickname)
                    .orElseGet(() -> userService.createOrUpdateUser(nickname, nickname, null));

            // Add user to plan if not already present
            if (plan.getUsers().stream().noneMatch(u -> u.getEmail().equalsIgnoreCase(nickname))) {
                plan.getUsers().add(user);
                updatePlan(plan);
                log.info("User '{}' added to plan '{}'", nickname, plan.getName());
                return true;
            } else {
                log.info("User '{}' is already in plan '{}'", nickname, plan.getName());
                return true; // Still return true as the end state is what was desired
            }
        } catch (Exception e) {
            log.error("Error adding user '{}' to plan {}: {}", nickname, planId, e.getMessage(), e);
            return false;
        }
    }
}
