// package distributed.systems.sd_cli_java.config;

// import org.apache.kafka.clients.admin.AdminClient;
// import org.apache.kafka.clients.admin.NewTopic;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.kafka.core.KafkaAdmin;

// import java.util.HashMap;
// import java.util.Map;
// import java.util.concurrent.ExecutionException;

// import org.apache.kafka.clients.admin.AdminClientConfig;

// import lombok.extern.slf4j.Slf4j;

// @Configuration
// @Slf4j
// public class DynamicTopicConfig {

//     @Value("${spring.kafka.bootstrap-servers}")
//     private String bootstrapServers;

//     @Bean
//     KafkaAdmin kafkaAdmin() {
//         Map<String, Object> configs = new HashMap<>();
//         configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//         return new KafkaAdmin(configs);
//     }

//     @Bean
//     AdminClient adminClient() {
//         return AdminClient.create(kafkaAdmin().getConfigurationProperties());
//     }

//     public void createDynamicTopic(String topicName) {
//         try {
//             boolean exists = adminClient().listTopics().names().get().contains(topicName);
//             if (!exists) {
//                 log.info("Creating new topic: {}", topicName);
//                 NewTopic newTopic = new NewTopic(topicName, 1, (short) 1);
//                 adminClient().createTopics(java.util.Collections.singleton(newTopic));
//                 log.info("Topic created: {}", topicName);
//             } else {
//                 log.info("Topic already exists: {}", topicName);
//             }
//         } catch (InterruptedException | ExecutionException e) {
//             log.error("Error creating topic {}: {}", topicName, e.getMessage(), e);
//         }
//     }
// }