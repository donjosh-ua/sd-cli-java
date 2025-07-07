// package distributed.systems.sd_cli_java.service;

// import java.util.List;

// import org.springframework.kafka.core.KafkaTemplate;
// import org.springframework.stereotype.Service;

// import distributed.systems.sd_cli_java.config.DynamicTopicConfig;
// import distributed.systems.sd_cli_java.model.dto.ExpenseCreatedNotificationDTO;
// import distributed.systems.sd_cli_java.model.entity.Expense;
// import distributed.systems.sd_cli_java.model.entity.Plan;
// import distributed.systems.sd_cli_java.repository.ExpenseRepository;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;

// @Service
// @RequiredArgsConstructor
// @Slf4j
// public class KafkaProducerService {

//     private final KafkaTemplate<String, Object> kafkaTemplate;
//     private final DynamicTopicConfig dynamicTopicConfig;
//     private final ExpenseRepository expenseRepository;

//     public void sendExpenseCreatedNotification(Expense expense) {

//         String topicName = "expense_notifications_" + expense.getPlan().getPlanId();

//         dynamicTopicConfig.createDynamicTopic(topicName);

//         // Get the plan
//         Plan plan = expense.getPlan();

//         // Calculate total expenses for the plan
//         float totalPlanExpense = calculateTotalPlanExpense(plan);

//         // Calculate per-user share based on total plan expenses
//         int userCount = plan.getUsers().size();
//         float sharePerUser = userCount > 0 ? totalPlanExpense / userCount : totalPlanExpense;

//         ExpenseCreatedNotificationDTO notification = ExpenseCreatedNotificationDTO.builder()
//                 .id(expense.getExpenseId())
//                 .name(expense.getName())
//                 .amount(sharePerUser)
//                 .date(expense.getDate())
//                 .type(expense.getType())
//                 .planId(expense.getPlan().getPlanId())
//                 .build();

//         log.info("Sending notification to topic {}: {}", topicName, notification);

//         for (int i = 0; i < 5; i++) {
//             kafkaTemplate.send(topicName, notification)
//                     .whenComplete((result, ex) -> {
//                         if (ex == null) {
//                             log.info("Notification sent successfully to topic {}", topicName);
//                         } else {
//                             log.error("Failed to send notification to topic {}: {}", topicName, ex.getMessage(), ex);
//                         }
//                     });
//         }
//     }

//     private float calculateTotalPlanExpense(Plan plan) {
//         List<Expense> expenses = expenseRepository.findByPlan(plan);

//         return expenses.stream()
//                 .map(Expense::getAmount)
//                 .reduce(0.0f, Float::sum);
//     }
// }