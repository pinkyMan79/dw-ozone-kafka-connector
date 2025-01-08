package one.terenin.okconnector;

import one.terenin.okconnector.service.DataPipelineService;
import org.apache.hadoop.ozone.client.OzoneClient;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;

import java.io.IOException;

@SpringBootApplication
public class OkConnectorApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(OkConnectorApplication.class, args);
        DataPipelineService dataPipelineService = run.getBean("dataPipelineService", DataPipelineService.class);
        try {
            dataPipelineService.loadDataToKafkaBinary();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public HttpClient dataPiplineClient() {
        return HttpClients.createDefault();
    }

}
