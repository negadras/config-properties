package com.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 
 * @since 2018-07-25
 */
class DefaultConfigConfigurerConfigSourcesTest extends AbstractConfigTest {

    @Value("${config-files.config-file.one.property.one:not-provided}")
    private String configFilesConfigFileOnePropertyOne;

    @Value("${config-files.config-file.one.property.two:not-provided}")
    private String configFilesConfigFileOnePropertyTwo;

    @Value("${config-files.config-file.two.property.one:not-provided}")
    private String configFilesConfigFileTwoPropertyOne;

    @Value("${config-files.config-file.two.property.two:not-provided}")
    private String configFilesConfigFileTwoPropertyTwo;

    @Value("${config-files.config-file.three.property.one:not-provided}")
    private String configFilesConfigFileThreePropertyOne;

    @Value("${config-files.config-file.three.property.two:not-provided}")
    private String configFilesConfigFileThreePropertyTwo;

    @Value("${config-files.config-file.four.property.one:not-provided}")
    private String configFilesConfigFileFourPropertyOne;

    @Value("${config-files.config-file.four.property.two:not-provided}")
    private String configFilesConfigFileFourPropertyTwo;

    @Value("${config-files.config-file.four.property.three:not-provided}")
    private String configFilesConfigFileFourPropertyThree;

    @Value("${config-file.1.property.1:not-provided}")
    private String configFileOnePropertyOne;

    @Value("${config-file.1.property.2:not-provided}")
    private String configFileOnePropertyTwo;

    @Value("${yaml-config-file.yaml-config-file-property:not-provided}")
    private String configFileYamlProperty;

    @Test
    void configFiles_getLoaded() {
        assertAll("Config files loaded",
                () -> assertEquals("value.1", configFilesConfigFileOnePropertyOne),
                () -> assertEquals("value.1", configFilesConfigFileOnePropertyTwo),
                () -> assertEquals("value.1", configFilesConfigFileTwoPropertyOne),
                () -> assertEquals("value.1", configFilesConfigFileTwoPropertyTwo),
                () -> assertEquals("value.1", configFilesConfigFileThreePropertyOne),
                () -> assertEquals("value.1", configFilesConfigFileThreePropertyTwo),
                () -> assertEquals("value.1", configFilesConfigFileFourPropertyOne),
                () -> assertEquals("value.1", configFilesConfigFileFourPropertyTwo),
                () -> assertEquals("value.2", configFilesConfigFileFourPropertyThree),
                () -> assertEquals("value.1", configFileOnePropertyOne),
                () -> assertEquals("value.1", configFileOnePropertyTwo),
                () -> assertEquals("value", configFileYamlProperty)
        );
    }
}
