package distributed.systems.sd_cli_java.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import distributed.systems.sd_cli_java.model.dto.expense.ExpenseAddedEvent;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpensePublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routingkey}")
    private String routingKey;

    public void publishExpenseAdded(ExpenseAddedEvent event) {
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
    }
}