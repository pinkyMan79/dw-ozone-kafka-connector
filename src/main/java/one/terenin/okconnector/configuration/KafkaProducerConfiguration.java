package one.terenin.okconnector.configuration;

import lombok.RequiredArgsConstructor;
import one.terenin.okconnector.configuration.propertysource.KafkaConfigurationPropertySource;
import one.terenin.okconnector.configuration.topic.KafkaTopicConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
@EnableKafka
@Import({KafkaTopicConfiguration.class})
@RequiredArgsConstructor
public class KafkaProducerConfiguration {

    private final KafkaConfigurationPropertySource propertySource;

    @Bean
    public ProducerFactory<String, String> producerFactoryJson() {
        return new DefaultKafkaProducerFactory<>(propertySource.forStringAsMap());
    }

    @Bean
    public ProducerFactory<String, byte[]> producerFactoryParquet() {
        return new DefaultKafkaProducerFactory<>(propertySource.forBinaryaAsMap());
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplateJson() {
        return new KafkaTemplate<>(producerFactoryJson());
    }

    @Bean
    public KafkaTemplate<String, byte[]> kafkaTemplateBinary() {
        return new KafkaTemplate<>(producerFactoryParquet());
    }

}
