package distributed.systems.sd_cli_java.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import distributed.systems.sd_cli_java.controller.WebSocketController;
import distributed.systems.sd_cli_java.model.dto.expense.ExpenseAddedEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ExpenseEventListener {

    private final WebSocketController webSocketController;
    private final ObjectMapper objectMapper;

    public ExpenseEventListener(WebSocketController webSocketController) {
        this.webSocketController = webSocketController;
        this.objectMapper = new ObjectMapper();
    }

    @RabbitListener(queues = "${rabbitmq.expense.queue}")
    public void onExpenseEventReceived(String message) {
        try {
            ExpenseAddedEvent event = objectMapper.readValue(message, ExpenseAddedEvent.class);
            log.info("📥 Received event for plan {}", event.getPlanId());
            webSocketController.notifyExpenseAdded(event.getPlanId().toString(), event);
        } catch (Exception e) {
            log.error("❌ Failed to parse ExpenseAddedEvent from message: {}", message, e);
        }
    }
}
