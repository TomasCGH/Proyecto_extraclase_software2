package co.edu.uco.backendvictus.infrastructure.secondary.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azure.messaging.webpubsub.WebPubSubServiceClientBuilder;

@Configuration
public class WebPubSubConfig {

    @Bean
    public WebPubSubServiceClientBuilder webPubSubServiceClientBuilder(
            @Value("${azure.webpubsub.connection-string}") final String connectionString,
            @Value("${azure.webpubsub.hub}") final String hubName) {
        return new WebPubSubServiceClientBuilder()
                .connectionString(connectionString)
                .hub(hubName);
    }
}
