package distributed.systems.sd_cli_java.service;

import java.util.stream.Collectors;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import distributed.systems.sd_cli_java.model.dto.ExpenseNotificationDTO;
import distributed.systems.sd_cli_java.model.entity.Expense;
import distributed.systems.sd_cli_java.model.entity.Plan;
import distributed.systems.sd_cli_java.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, ExpenseNotificationDTO> kafkaTemplate;
    private final ExpenseRepository expenseRepository; // Use repository directly instead of service

    public void sendExpenseNotification(Expense expense) {
        if (expense.getPlan() == null || expense.getPlan().getUsers().isEmpty()) {
            log.info("No users to notify about expense: {}", expense.getName());
            return;
        }

        Plan plan = expense.getPlan();

        // Calculate total plan expense directly using repository
        Float totalPlanExpense = calculateTotalExpensesForPlan(plan);

        // Create notification DTO
        ExpenseNotificationDTO notification = ExpenseNotificationDTO.builder()
                .expenseId(expense.getExpenseId())
                .expenseName(expense.getName())
                .amount(expense.getAmount())
                .date(expense.getDate())
                .type(expense.getType())
                .userId(expense.getUser().getUserId())
                .username(expense.getUser().getUsername())
                .planId(plan.getPlanId())
                .planName(plan.getName())
                .totalPlanExpense(totalPlanExpense)
                .affectedUserIds(plan.getUsers().stream()
                        .map(user -> user.getUserId())
                        .collect(Collectors.toList()))
                .build();

        // Send to Kafka topic
        log.info("Sending expense notification for: {}", expense.getName());
        kafkaTemplate.send("join_plan", notification);
    }

    // Move the calculation method here to avoid circular dependency
    private Float calculateTotalExpensesForPlan(Plan plan) {
        return expenseRepository.findByPlan(plan).stream()
                .map(Expense::getAmount)
                .reduce(0.0f, Float::sum);
    }
}