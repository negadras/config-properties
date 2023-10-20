package com.config;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 
 * @since 2018-07-25
 */
class DefaultConfigConfigurerPropertySourcesTest extends AbstractConfigTest {

    @Value("${sources.source.1.property.1:not-provided}")
    private String sourcesSource1Property1;

    @Value("${sources.source.1.property.2:not-provided}")
    private String sourcesSource1Property2;

    @Value("${sources.source.2.property.1:not-provided}")
    private String sourcesSource2Property1;

    @Value("${sources.source.2.property.2:not-provided}")
    private String sourcesSource2Property2;

    @Value("${sources.source.2.property.3:not-provided}")
    private String sourcesSource2Property3;

    @Value("${source.1.property.1:not-provided}")
    private String source1Property1;

    @Value("${source.1.property.2:not-provided}")
    private String source1Property2;

    @Test
    void propertySourcesDefaultConfig() {
        assertAll("Property Sources Default Config",
                () -> assertEquals("value.1", sourcesSource1Property1),
                () -> assertEquals("value.1", sourcesSource1Property2),
                () -> assertEquals("value.1", sourcesSource2Property1),
                () -> assertEquals("value.1", sourcesSource2Property2),
                () -> assertEquals("value.2", sourcesSource2Property3),
                () -> assertEquals("value.1", source1Property1),
                () -> assertEquals("value.1", source1Property2)
        );
    }
}
