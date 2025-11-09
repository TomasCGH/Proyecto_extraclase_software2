package co.edu.uco.backendvictus.infrastructure.secondary.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;

@Configuration
public class AzureServiceBusConfig {

    @Bean
    public ServiceBusClientBuilder serviceBusClientBuilder(
            @Value("${azure.servicebus.connection-string}") final String connectionString) {
        return new ServiceBusClientBuilder().connectionString(connectionString);
    }
}
