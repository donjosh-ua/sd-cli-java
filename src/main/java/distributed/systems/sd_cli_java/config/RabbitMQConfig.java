package distributed.systems.sd_cli_java.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    Queue expenseQueue(@Value("${rabbitmq.expense.queue}") String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    FanoutExchange expenseExchange(@Value("${rabbitmq.exchange}") String exchangeName) {
        return new FanoutExchange(exchangeName);
    }

    @Bean
    Binding expenseBinding(Queue expenseQueue, FanoutExchange expenseExchange) {
        return BindingBuilder.bind(expenseQueue).to(expenseExchange);
    }
}
