package distributed.systems.sd_cli_java.controller;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyExpenseAdded(String planId, Object payload) {
        String destination = "/topic/plan/" + planId;
        messagingTemplate.convertAndSend(destination, payload);
        log.info("📢 Sent update to {}", destination);
    }
}
