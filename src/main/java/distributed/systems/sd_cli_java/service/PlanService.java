package distributed.systems.sd_cli_java.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import distributed.systems.sd_cli_java.mapper.PlanMapper;
import distributed.systems.sd_cli_java.model.dto.plan.PlanDTO;
import distributed.systems.sd_cli_java.model.entity.Expense;
import distributed.systems.sd_cli_java.model.entity.Plan;
import distributed.systems.sd_cli_java.model.entity.User;
import distributed.systems.sd_cli_java.repository.ExpenseRepository;
import distributed.systems.sd_cli_java.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PlanService {

    private final PlanMapper planMapper;
    private final PlanRepository planRepository;
    private final ExpenseRepository expenseRepository;

    public PlanDTO createPlan(PlanDTO planDto) {

        if (planDto.getOwner() == null || planDto.getOwner().isEmpty()) {
            throw new IllegalArgumentException("Plan owner cannot be null or empty");
        }

        planDto.setDate(LocalDateTime.now());
        planDto.setStatus(true);

        Plan planEntity = planRepository.save(planMapper.toEntity(planDto));

        log.info("Plan created with name: {}", planDto.getName());

        return planMapper.toDto(planEntity);
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
        plan.getParticipants().add(user);
        return planRepository.save(plan);
    }

    public Plan removeUserFromPlan(Plan plan, User user) {
        plan.getParticipants().remove(user);
        return planRepository.save(plan);
    }

    public Plan addExpenseToPlan(Plan plan, Expense expense) {
        expense.setPlan(plan);
        expenseRepository.save(expense);
        return planRepository.findById(plan.getPlanId()).orElse(plan);
    }

    public List<Plan> findPlansByDateAfter(LocalDateTime date) {
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    public List<Plan> findPlansByUser(User user) {
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    public Optional<Plan> findByName(String name) {
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    public String generateInvitationLink(Long planId) {
        // In a real app, you'd probably have a more sophisticated way to generate and
        // validate links
        return "http://yourapp.com/join/" + planId + "/" + UUID.randomUUID().toString();
    }

    public Long countPlansByUser(User user) {
        throw new UnsupportedOperationException("Method not implemented yet");
    }

}
