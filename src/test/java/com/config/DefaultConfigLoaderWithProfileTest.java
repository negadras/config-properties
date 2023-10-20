package com.config;

import org.example.app.TestApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 
 * @since 2018-07-25
 */
@SpringBootTest(
        classes = TestApplication.class,
        properties = {
                "spring.profiles.active=some-env"
        }
)
class DefaultConfigLoaderWithProfileTest extends DefaultConfigLoaderTest {

    @Test
    @Override
    public void autoLoadedConfigFiles() {
        assertAll("",
                () -> assertEquals("value.1", configFileOnePropertyOne),
                () -> assertEquals("value.1", configFileOnePropertyTwo),
                () -> assertEquals("value for some-env", configFileYamlProperty),
                () -> assertEquals("not-provided", configFileYamlPropertyNotToBeLoaded)
        );
    }
}
