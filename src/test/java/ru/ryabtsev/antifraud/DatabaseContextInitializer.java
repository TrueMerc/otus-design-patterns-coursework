package ru.ryabtsev.antifraud;

import java.util.Map;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class DatabaseContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private final PostgreSQLContainer<?> postgreSQLContainer;


    public DatabaseContextInitializer() {
        postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
        postgreSQLContainer.start();
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        TestPropertyValues.of(
                Map.of(
                        "spring.datasource.url", postgreSQLContainer.getJdbcUrl(),
                        "spring.datasource.username", postgreSQLContainer.getUsername(),
                        "spring.datasource.password", postgreSQLContainer.getPassword()
                )
        ).applyTo(applicationContext);
    }
}
