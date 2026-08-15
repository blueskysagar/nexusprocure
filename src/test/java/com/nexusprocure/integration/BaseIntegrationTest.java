package com.nexusprocure.integration;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("nexusprocure")
            .withUsername("postgres")
            .withPassword("postgres");
     @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry){
         registry.add("spring.datasource.url", postgres::getJdbcUrl);
         registry.add("spring.datasource.username", postgres::getUsername);
         registry.add("spring.datasource.password", postgres::getPassword);

     }
     @Autowired
    protected DataBaseCleanUp dataBaseCleanUp;
     @AfterEach
    void cleanUpDatabase(){
         dataBaseCleanUp.clean();
     }
}
