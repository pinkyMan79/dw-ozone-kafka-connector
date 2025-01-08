package one.terenin.okconnector.configuration.propertysource;

import lombok.AccessLevel;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PropertySource("classpath:/application.yaml")
public class KafkaConfigurationPropertySource {

    String bootstrapServers;
    Class<?> keySerializerString;
    Class<?> keySerializerBinary;
    Class<?> valueSerializerString;
    Class<?> valueSerializerBinary;
    Long lingerMs;

    @SneakyThrows
    public KafkaConfigurationPropertySource(@Value("${bootstrap.server.host}") String bootstrapServers,
                                            @Value("${key.serializer.binary}") String keySerializerBinary,
                                            @Value("${value.serializer.binary}") String valueSerializerBinary,
                                            @Value("${key.serializer.string}") String keySerializerString,
                                            @Value("${value.serializer.string}") String valueSerializerString,
                                            @Value("${linger.ms}") Long lingerMs) {
        this.bootstrapServers = bootstrapServers;
        this.keySerializerBinary = Class.forName(keySerializerBinary);
        this.valueSerializerBinary = Class.forName(valueSerializerBinary);
        this.keySerializerString = Class.forName(keySerializerString);
        this.valueSerializerString = Class.forName(valueSerializerString);
        this.lingerMs = lingerMs;
    }

    public Map<String, Object> forStringAsMap() {
        return Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers,
                ProducerConfig.LINGER_MS_CONFIG, this.lingerMs,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, this.keySerializerString,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, this.valueSerializerString
        );
    }

    public Map<String, Object> forBinaryaAsMap() {
        return Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers,
                ProducerConfig.LINGER_MS_CONFIG, this.lingerMs,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, this.keySerializerBinary,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, this.valueSerializerBinary
        );
    }
}
