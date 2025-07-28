package distributed.systems.sd_cli_java.service;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import distributed.systems.sd_cli_java.config.RabbitMQConfig;

@Service
public class ExpensePublisher {

    private final AmqpTemplate rabbitTemplate;

    public ExpensePublisher(AmqpTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishExpenseUpdate(String message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "", message);
    }
}
