package distributed.systems.sd_cli_java.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import distributed.systems.sd_cli_java.config.DynamicTopicConfig;
import distributed.systems.sd_cli_java.model.dto.ExpenseCreatedNotificationDTO;
import distributed.systems.sd_cli_java.model.entity.Expense;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final DynamicTopicConfig dynamicTopicConfig;

    public void sendExpenseCreatedNotification(Expense expense) {
        String topicName = "expense_notifications_" + expense.getPlan().getPlanId();

        // Ensure the topic exists
        dynamicTopicConfig.createDynamicTopic(topicName);

        ExpenseCreatedNotificationDTO notification = ExpenseCreatedNotificationDTO.builder()
                .id(expense.getExpenseId())
                .name(expense.getName())
                .amount(expense.getAmount())
                .date(expense.getDate())
                .type(expense.getType())
                .planId(expense.getPlan().getPlanId())
                .build();

        log.info("Sending notification to topic {}: {}", topicName, notification);

        kafkaTemplate.send(topicName, notification)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Notification sent successfully to topic {}", topicName);
                    } else {
                        log.error("Failed to send notification to topic {}: {}", topicName, ex.getMessage(), ex);
                    }
                });
    }
}