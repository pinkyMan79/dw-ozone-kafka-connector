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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

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
        stringKafkaTemplate.send("jsonTopic", 0, "0", new String(execute.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8));
        stringKafkaTemplate.send("jsonTopic", 1, "1", new String(execute.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8));
        stringKafkaTemplate.send("jsonTopic", 2, "2", new String(execute.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8));
    }

    public void loadDataToKafkaBinary() throws IOException {
        int countToGen = 10;
        HttpGet get = new HttpGet("http://localhost:8080/datagen/parquet/" + countToGen);
        HttpResponse execute = client.execute(get);
        byteKafkaTemplate.send("parquetTopic", 0, "0", execute.getEntity().getContent().readAllBytes());
        byteKafkaTemplate.send("parquetTopic", 1, "1", execute.getEntity().getContent().readAllBytes());
        byteKafkaTemplate.send("parquetTopic", 2, "2", execute.getEntity().getContent().readAllBytes());
    }
}
