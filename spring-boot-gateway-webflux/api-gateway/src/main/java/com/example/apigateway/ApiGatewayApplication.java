package com.example.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;

import java.util.Arrays;
import java.util.stream.StreamSupport;

@EnableDiscoveryClient
@SpringBootApplication
public class ApiGatewayApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiGatewayApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}

    // @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        final var env = event.getApplicationContext()
                .getEnvironment();

        LOGGER.info("Active profiles: {}", Arrays.toString(env.getActiveProfiles()));

        final var sources = ((AbstractEnvironment) env).getPropertySources();

        StreamSupport.stream(sources.spliterator(), false)
                .filter(ps -> ps instanceof EnumerablePropertySource)
                .map(ps -> ((EnumerablePropertySource) ps).getPropertyNames())
                .flatMap(Arrays::stream)
                .distinct()
                .filter(prop -> !(prop.contains("credentials") || prop.contains("password")))
                .forEach(prop -> LOGGER.info("{}: {}", prop, env.getProperty(prop)));
    }
}


