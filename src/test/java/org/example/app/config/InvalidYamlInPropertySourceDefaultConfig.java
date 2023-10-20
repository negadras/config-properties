package org.example.app.config;

import com.example.configurer.AbstractAnnotationDefaultConfigConfigurer;
import org.springframework.context.annotation.PropertySource;


@PropertySource(
        name = "yaml-in-property-source",
        value = "classpath:/example/property-source.yml"
)
public class InvalidYamlInPropertySourceDefaultConfig extends AbstractAnnotationDefaultConfigConfigurer {
}
