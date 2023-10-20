package com.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 
 * @since 2018-07-25
 */
class DefaultConfigLoaderTest extends AbstractConfigTest {

    @Value("${auto-loaded.config-file.1.property.1:not-provided}")
    protected String configFileOnePropertyOne;

    @Value("${auto-loaded.config-file.1.property.2:not-provided}")
    protected String configFileOnePropertyTwo;

    @Value("${auto-loaded.yaml-config-file.yaml-config-file-property:not-provided}")
    protected String configFileYamlProperty;

    @Value("${auto-loaded.yaml-config-file.some-other-env-property:not-provided}")
    protected String configFileYamlPropertyNotToBeLoaded;

    @Test
    void autoLoadedConfigFiles() {
        assertAll("",
                () -> assertEquals("value.1", configFileOnePropertyOne),
                () -> assertEquals("value.1", configFileOnePropertyTwo),
                () -> assertEquals("default value", configFileYamlProperty),
                () -> assertEquals("not-provided", configFileYamlPropertyNotToBeLoaded)
        );
    }
}
