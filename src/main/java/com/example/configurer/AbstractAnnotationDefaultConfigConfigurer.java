package com.example.configurer;

import com.example.ConfigSource;
import com.example.ConfigSources;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.config.ConfigDataEnvironmentPostProcessor;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * <p>
 * {@link AbstractAnnotationDefaultConfigConfigurer} is a helper class
 * to simplify the setup of a default config for e.g. a library.
 * </p>
 * <p>
 * The default config will be added after the application's own config,
 * but before Spring's default config.
 * Therefore, you can overwrite any value within your application's config.
 * </p>
 * <p>
 * <strong>Why not using {@link PropertySource {@code @PropertySource}}?</strong>
 * </p>
 * Unfortunately, {@link PropertySource} has some short-comings and is e.g.
 * not available at any {@code ConditionalOn} annotations like {@link ConditionalOnProperty}.
 * Also it is not possible ot use it with other config file types other than {@code .properties} files.
 * This class also supports the use of {@link ConfigSource} which supports any.
 * </p>
 * <p>
 * <strong>I need a specific order for different default configs -- how can I do that?</strong>
 * </p>
 * <p>
 * We implement the {@link Ordered} interface using the default ordering
 * and you can override the provided value by overriding {@link #getOrder()}. Be aware of
 * that this listener needs to be executed after {@link ConfigDataEnvironmentPostProcessor}
 * and therefore needs a greater value than {@link ConfigDataEnvironmentPostProcessor#ORDER its order value}.
 * </p>
 * <p>
 * You also need to register your {@link EnvironmentPostProcessor} at the {@code META-INF/spring.factories} file.
 * {@code org.springframework.boot.env.EnvironmentPostProcessor=[YOUR_CLASS]}
 * </p>
 *
 * 
 * @see PropertySources
 * @see PropertySource
 * @see ConfigSources
 * @see ConfigSource
 * @see Ordered
 * @see EnvironmentPostProcessor
 * @see ConfigDataEnvironmentPostProcessor
 * @see ConfigDataEnvironmentPostProcessor#APPLICATION_CONFIGURATION_PROPERTY_SOURCE_NAME
 * @see ConfigDataEnvironmentPostProcessor#ORDER
 * @since 2018-03-22
 */
@Slf4j
public abstract class AbstractAnnotationDefaultConfigConfigurer
        extends DefaultConfigConfigurer
        implements EnvironmentPostProcessor, Ordered {

    public static final int DEFAULT_ORDER = 0;

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        PropertySources propertySources = getClass().getAnnotation(PropertySources.class);
        PropertySource propertySource = getClass().getAnnotation(PropertySource.class);
        ConfigSources configSources = getClass().getAnnotation(ConfigSources.class);
        ConfigSource configSource = getClass().getAnnotation(ConfigSource.class);

        insertDefaultConfigs(environment, merge(configSources, configSource), merge(propertySources, propertySource));
    }

    protected PropertySource[] merge(PropertySources propertySources, PropertySource propertySource) {
        if (propertySource == null) {
            return propertySources != null ? propertySources.value() : null;
        }

        if (propertySources == null) {
            return new PropertySource[]{propertySource};
        }

        PropertySource[] merged = new PropertySource[propertySources.value().length + 1];
        System.arraycopy(propertySources.value(), 0, merged, 0, propertySources.value().length);
        merged[merged.length - 1] = propertySource;

        return merged;
    }

    protected ConfigSource[] merge(ConfigSources configSources, ConfigSource configSource) {
        if (configSource == null) {
            return configSources != null ? configSources.value() : null;
        }

        if (configSources == null) {
            return new ConfigSource[]{configSource};
        }

        ConfigSource[] merged = new ConfigSource[configSources.value().length + 1];
        System.arraycopy(configSources.value(), 0, merged, 0, configSources.value().length);
        merged[merged.length - 1] = configSource;

        return merged;
    }
}
