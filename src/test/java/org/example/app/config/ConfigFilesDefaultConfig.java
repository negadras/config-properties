package org.example.app.config;


import com.example.ConfigSource;
import com.example.ConfigSources;
import com.example.configurer.AbstractAnnotationDefaultConfigConfigurer;

@ConfigSources({
        @ConfigSource(
                name = "config-files_config-file-1",
                value = "classpath:/example/config-files/config-file-1.properties"
        ),
        @ConfigSource(
                name = "config-files_config-file-2",
                value = {
                        "classpath:/example/config-files/config-file-2-1.properties",
                        "classpath:/example/config-files/config-file-2-2.properties"
                }
        ),
        @ConfigSource(
                name = "config-files_config-file-3",
                value = {
                        "classpath:/example/config-files/config-file-3.yaml"
                }
        ),
        @ConfigSource(
                name = "config-files_config-file-4",
                value = {
                        "classpath:/example/config-files/config-file-4-1.yml",
                        "classpath:/example/config-files/config-file-4-2.yml"
                }
        )
})
@ConfigSource(
        name = "config-file",
        value = {
                "classpath:/example/config-file.properties",
                "classpath:/example/config-file.yaml",
        }
)
public class ConfigFilesDefaultConfig extends AbstractAnnotationDefaultConfigConfigurer {
}
