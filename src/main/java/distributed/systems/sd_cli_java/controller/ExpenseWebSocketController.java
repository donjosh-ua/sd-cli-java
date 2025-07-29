package distributed.systems.sd_cli_java.controller;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import distributed.systems.sd_cli_java.model.dto.expense.ExpenseCreatedNotificationDTO;

@Controller
public class ExpenseWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public ExpenseWebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendExpenseUpdate(String planId, ExpenseCreatedNotificationDTO dto) {
        messagingTemplate.convertAndSend("/topic/plan/" + planId, dto);
    }
}
