package one.terenin.okconnector.configuration.topic;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.internals.Topic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
// 3 partitions bijectively correlated with s networks (0 - reactive, 1 - gRpc, 2 - http3)
public class KafkaTopicConfiguration {

    @Bean
    public NewTopic stringTopic() {
        return TopicBuilder.name("jsonTopic")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic binaryTopic() {
        return TopicBuilder.name("parquetTopic")
                .partitions(3)
                .replicas(1)
                .build();
    }

}
