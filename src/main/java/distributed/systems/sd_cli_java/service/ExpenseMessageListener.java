package distributed.systems.sd_cli_java.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class ExpenseMessageListener {

    private final SimpMessagingTemplate messagingTemplate;

    public ExpenseMessageListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitListener(queues = "#{queue.name}")
    public void receive(String message) {
        // This will forward the message to all clients subscribed to /topic/expenses
        messagingTemplate.convertAndSend("/topic/expenses", message);
    }
}
