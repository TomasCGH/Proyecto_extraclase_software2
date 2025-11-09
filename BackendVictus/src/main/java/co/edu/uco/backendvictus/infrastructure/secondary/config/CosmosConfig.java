package co.edu.uco.backendvictus.infrastructure.secondary.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.CosmosAsyncClientBuilder;
import com.azure.cosmos.ConsistencyLevel;

@Configuration
public class CosmosConfig {

    @Bean
    public CosmosAsyncClient cosmosAsyncClient(@Value("${azure.cosmos.endpoint}") final String endpoint,
            @Value("${azure.cosmos.key}") final String key) {
        return new CosmosAsyncClientBuilder()
                .endpoint(endpoint)
                .key(key)
                .consistencyLevel(ConsistencyLevel.EVENTUAL)
                .buildAsyncClient();
    }
}
