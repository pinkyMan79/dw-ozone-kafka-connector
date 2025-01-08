package one.terenin.okconnector.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.terenin.okconnector.common.OzoneNames;
import org.apache.hadoop.ozone.client.OzoneBucket;
import org.apache.hadoop.ozone.client.OzoneKey;
import org.apache.hadoop.ozone.client.OzoneKeyDetails;
import org.apache.hadoop.ozone.client.OzoneVolume;
import org.apache.hadoop.ozone.client.io.OzoneInputStream;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataPipelineService {

    private final HttpClient client;
    private final KafkaTemplate<String, String> stringKafkaTemplate;
    private final KafkaTemplate<String, byte[]> byteKafkaTemplate;

    public void loadDataToKafkaJson() throws IOException {
        int countToGen = 10;
        HttpGet get = new HttpGet("http://localhost:8080/datagen/json/" + countToGen);
        HttpResponse execute = client.execute(get);
        String data = new String(execute.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
        CompletableFuture<SendResult<String, String>> p1 = stringKafkaTemplate.send("jsonTopic",
                0,
                "0",
                data);
        CompletableFuture<SendResult<String, String>> p2 = stringKafkaTemplate.send("jsonTopic",
                1,
                "1",
                data);
        CompletableFuture<SendResult<String, String>> p3 = stringKafkaTemplate.send("jsonTopic",
                2,
                "2",
                data);
    }

    public void loadDataToKafkaBinary() throws IOException {
        int countToGen = 10;
        HttpGet get = new HttpGet("http://localhost:8080/datagen/parquet/" + countToGen);
        HttpResponse execute = client.execute(get);
        byte[] bytes = execute.getEntity().getContent().readAllBytes();
        byteKafkaTemplate.send("parquetTopic", 0, "0", bytes);
        byteKafkaTemplate.send("parquetTopic", 1, "1", bytes);
        byteKafkaTemplate.send("parquetTopic", 2, "2", bytes);
    }
}
