package org.example.app.config;

import com.example.configurer.AbstractAnnotationDefaultConfigConfigurer;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * 
 * @since 2018-07-25
 */
@PropertySources({
        @PropertySource(
                name = "property-sources_property-source-1",
                value = "classpath:/example/property-sources/property-source-1.properties"
        ),
        @PropertySource(
                name = "property-sources_property-source-2",
                value = {
                        "classpath:/example/property-sources/property-source-2-1.properties",
                        "classpath:/example/property-sources/property-source-2-2.properties"
                }
        )
})
@PropertySource(
        name = "property-source",
        value = "classpath:/example/property-source.properties"
)
public class PropertySourcesDefaultConfig extends AbstractAnnotationDefaultConfigConfigurer {
}
