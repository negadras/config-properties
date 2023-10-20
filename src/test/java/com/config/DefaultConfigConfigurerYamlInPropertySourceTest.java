package com.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 
 * @since 2018-07-25
 */
class DefaultConfigConfigurerYamlInPropertySourceTest extends AbstractConfigTest {

    @Value("${yaml-property-source.yaml-property-source-property:not-provided}")
    private String propertySourceYaml;

    @Value("${yaml-property-source:not-provided}")
    private String propertySourceYamlInvalidProperty1;

    @Value("${yaml-property-source-property:not-provided}")
    private String propertySourceYamlInvalidProperty2;

    @Test
    void yamlInPropertySource_parsedWronglyAsPropertiesFile() {
        assertAll("",
                () -> assertEquals("not-provided", propertySourceYaml),
                () -> assertEquals("", propertySourceYamlInvalidProperty1),
                () -> assertEquals("value", propertySourceYamlInvalidProperty2)
        );
    }
}
