package com.config;

import org.example.app.TestApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 
 * @since 2018-07-25
 */
@SpringBootTest(
        classes = TestApplication.class
)
@TestPropertySource(locations = "classpath:application.properties")
public abstract class AbstractConfigTest {

    @Value("${app.override:not-provided}")
    private String appOverride;

    @Test
    public void appOverridesDefaultConfig() {
        assertEquals("success", appOverride);
    }

}