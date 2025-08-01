package distributed.systems.sd_cli_java.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import distributed.systems.sd_cli_java.common.Util;
import distributed.systems.sd_cli_java.mapper.ExpenseMapper;
import distributed.systems.sd_cli_java.mapper.PlanMapper;
import distributed.systems.sd_cli_java.mapper.UserMapper;
import distributed.systems.sd_cli_java.model.dto.expense.ExpenseDTO;
import distributed.systems.sd_cli_java.model.dto.plan.JoinPlanDTO;
import distributed.systems.sd_cli_java.model.dto.plan.PlanDTO;
import distributed.systems.sd_cli_java.model.dto.user.UserResponseDTO;
import distributed.systems.sd_cli_java.model.entity.Expense;
import distributed.systems.sd_cli_java.model.entity.Plan;
import distributed.systems.sd_cli_java.model.entity.User;
import distributed.systems.sd_cli_java.repository.ExpenseRepository;
import distributed.systems.sd_cli_java.repository.PlanRepository;
import distributed.systems.sd_cli_java.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PlanService {

    private final PlanMapper planMapper;
    private final UserMapper userMapper;
    private final ExpenseMapper expenseMapper;
    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;

    @Transactional
    public PlanDTO createPlan(PlanDTO planDto) {

        if (planDto.getOwner() == null || planDto.getOwner().isEmpty())
            throw new IllegalArgumentException("Plan owner cannot be null or empty");

        User owner = userRepository.findByEmail(planDto.getOwner())
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));

        Plan planEntity = planMapper.toEntity(planDto);
        String code = Util.generatePlanCode();

        while (planRepository.findByCode(code).isPresent())
            code = Util.generatePlanCode();

        planEntity.setCode(code);
        planEntity.setDate(LocalDateTime.now());
        planEntity.setStatus(true);
        planEntity.setParticipants(List.of(owner));

        log.info("Plan created with name: {}", planDto.getName());

        return planMapper.toDto(planRepository.save(planEntity));
    }

    public PlanDTO updatePlan(PlanDTO planDto) {

        Plan planEntity = planRepository.findById(planDto.getPlanId())
                .orElseThrow(() -> new IllegalArgumentException("Plan not found"));

        if (planDto.getName() != null)
            planEntity.setName(planDto.getName());

        if (planDto.getDescription() != null)
            planEntity.setDescription(planDto.getDescription());

        if (planDto.getCategory() != null)
            planEntity.setCategory(planDto.getCategory());

        if (planDto.getStatus() != null)
            planEntity.setStatus(planDto.getStatus());

        planRepository.save(planEntity);

        log.info("Plan updated with id: {}", planDto.getPlanId());

        return planMapper.toDto(planEntity);
    }

    public Optional<PlanDTO> findById(Long id) {

        if (!planRepository.existsById(id))
            throw new IllegalArgumentException("Plan not found");

        return planRepository.findById(id).map(planMapper::toDto);
    }

    public List<PlanDTO> findAllPlans() {
        return planMapper.toDtoList(planRepository.findAll());
    }

    public List<UserResponseDTO> findParticipantsByPlanId(Long planId) {

        if (!planRepository.existsById(planId))
            throw new IllegalArgumentException("Plan not found");

        List<User> participants = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found"))
                .getParticipants();

        return userMapper.toResponseDTOList(participants);
    }

    public List<ExpenseDTO> findExpensesByPlanId(Long planId) {

        if (!planRepository.existsById(planId))
            throw new IllegalArgumentException("Plan not found");

        List<Expense> expenses = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found"))
                .getExpenses();

        return expenseMapper.toDtoList(expenses);
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

    public String generateInvitationLink(Long planId) {
        // In a real app, you'd probably have a more sophisticated way to generate and
        // validate links
        return "http://yourapp.com/join/" + planId + "/" + UUID.randomUUID().toString();
    }

    public List<PlanDTO> findPlansByParticipantId(String userId) {
        return planMapper.toDtoList(planRepository.findByParticipantId(userId));
    }

    public PlanDTO joinPlan(JoinPlanDTO joinPlan) {

        Plan plan = planRepository.findByCode(joinPlan.getCode())
                .orElseThrow(() -> new IllegalArgumentException("Plan not found"));

        User user = userRepository.findByEmail(joinPlan.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (plan.getParticipants().stream().anyMatch(p -> p.getEmail().equals(user.getEmail())))
            throw new IllegalArgumentException("User already a participant in this plan");

        plan.getParticipants().add(user);
        planRepository.save(plan);

        log.info("User {} joined plan {}", user.getEmail(), plan.getName());

        return planMapper.toDto(plan);
    }

    public Object quitPlan(JoinPlanDTO joinPlan) {

        Plan plan = planRepository.findByCode(joinPlan.getCode())
                .orElseThrow(() -> new IllegalArgumentException("Plan not found"));

        User user = userRepository.findByEmail(joinPlan.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (plan.getParticipants().stream().noneMatch(p -> p.getEmail().equals(user.getEmail())))
            throw new IllegalArgumentException("User is not a participant in this plan");

        plan.getParticipants().remove(user);
        planRepository.save(plan);

        log.info("User {} quit plan {}", user.getEmail(), plan.getName());

        return planMapper.toDto(plan);
    }

}
