package distributed.systems.sd_cli_java.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import distributed.systems.sd_cli_java.model.dto.ExpenseNotificationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {

    private final PlanService planService;

    @KafkaListener(topics = "join_plan", containerFactory = "kafkaListenerContainerFactory")
    public void consumeExpenseNotification(ExpenseNotificationDTO notification) {
        try {
            log.info("Received join plan request for user: {} to plan: {}",
                    notification.getUsername(), notification.getPlanId());

            // Validate required fields
            if (notification.getPlanId() == null || notification.getUsername() == null) {
                log.warn("Cannot process join plan request: missing username or planId");
                return;
            }

            // Call the plan service to add user to plan
            boolean success = planService.addUserToPlanByUsername(
                    notification.getUsername(),
                    notification.getPlanId());

            if (success) {
                log.info("Successfully processed join plan request for user '{}' to plan {}",
                        notification.getUsername(), notification.getPlanId());
            } else {
                log.warn("Failed to process join plan request (user might already be in plan or plan not found)");
            }
        } catch (Exception e) {
            log.error("Error processing join plan notification: {}", e.getMessage(), e);
        }
    }
}