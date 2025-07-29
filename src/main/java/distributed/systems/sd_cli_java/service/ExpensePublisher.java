package distributed.systems.sd_cli_java.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import distributed.systems.sd_cli_java.model.dto.expense.ExpenseAddedEvent;
import distributed.systems.sd_cli_java.model.dto.expense.ExpenseRequestDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpensePublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routingkey}")
    private String routingKey;

    public void publishExpenseAdded(ExpenseRequestDTO expense) {
        try {
            String json = new ObjectMapper().writeValueAsString(expense);
            rabbitTemplate.convertAndSend(exchange, routingKey, json);
        } catch (Exception e) {
            throw new RuntimeException("Failed to publish expense added event", e);
        }
    }
}