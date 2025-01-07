package one.terenin.okconnector.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.terenin.okconnector.common.OzoneNames;
import org.apache.hadoop.ozone.client.OzoneBucket;
import org.apache.hadoop.ozone.client.OzoneKey;
import org.apache.hadoop.ozone.client.OzoneKeyDetails;
import org.apache.hadoop.ozone.client.OzoneVolume;
import org.apache.hadoop.ozone.client.io.OzoneInputStream;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Iterator;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty("datagen.ozone")
public class DataPipelineServiceOzone {

    private final OzoneVolume ozoneVolume;
    private final KafkaTemplate<String, String> stringKafkaTemplate;
    private final KafkaTemplate<String, byte[]> byteKafkaTemplate;

    public void loadDataToKafkaJson() throws IOException {
        OzoneBucket bucket = ozoneVolume.getBucket(OzoneNames.ozoneJSONBucketName);

        Iterator<? extends OzoneKey> iterator = bucket.listKeys("");
        while (iterator.hasNext()) {
            OzoneKey next = iterator.next();
            if (next.getName().contains("json")) {
                OzoneKeyDetails key = bucket.getKey(next.getName());
                OzoneInputStream content = key.getContent();
                String dataline = new String(content.readAllBytes());
                stringKafkaTemplate.send("jsonTopic", 0, "0", dataline);
                stringKafkaTemplate.send("jsonTopic", 1, "1", dataline);
                stringKafkaTemplate.send("jsonTopic", 2, "2", dataline);
            } else {
                log.warn("In the jsons bucket detected key with unmatched name: {}, \n {}", next.getName(), next.getMetadata());
            }
        }
    }

    public void loadDataToKafkaBinary() throws IOException {
        OzoneBucket bucket = ozoneVolume.getBucket(OzoneNames.ozoneBucketName);

        Iterator<? extends OzoneKey> iterator = bucket.listKeys("");
        while (iterator.hasNext()) {
            OzoneKey next = iterator.next();
            if (next.getName().contains("parquet")) {
                OzoneKeyDetails key = bucket.getKey(next.getName());
                OzoneInputStream content = key.getContent();
                byte[] bytes = content.readAllBytes();
                byteKafkaTemplate.send("parquetTopic", 0, "0", bytes);
                byteKafkaTemplate.send("parquetTopic", 1, "1", bytes);
                byteKafkaTemplate.send("parquetTopic", 2, "2", bytes);
            } else {
                log.warn("In the binary-data bucket detected key with unmatched name: {}, \n {}", next.getName(), next.getMetadata());
            }
        }
    }

}
