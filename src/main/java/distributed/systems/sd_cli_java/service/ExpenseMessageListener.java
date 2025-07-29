package distributed.systems.sd_cli_java.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import distributed.systems.sd_cli_java.controller.ExpenseWebSocketController;
import distributed.systems.sd_cli_java.model.dto.expense.ExpenseCreatedNotificationDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExpenseMessageListener {

    private final ObjectMapper objectMapper;
    private final ExpenseWebSocketController controller;

    public ExpenseMessageListener(ObjectMapper objectMapper, ExpenseWebSocketController controller) {
        this.objectMapper = objectMapper;
        this.controller = controller;
    }

    @RabbitListener(queues = "${rabbitmq.expense.queue}")
    public void handleExpenseAddedEvent(String message) {
        try {
            ExpenseCreatedNotificationDTO dto = objectMapper.readValue(message, ExpenseCreatedNotificationDTO.class);
            controller.sendExpenseUpdate(dto.getId().toString(), dto);
        } catch (Exception e) {
            log.error("Failed to process message: {}", e.getMessage());
        }
    }
}
