package com.example.configurer;

import com.example.ConfigSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.stream.StreamSupport;

/**
 * <p>
 * {@link DefaultConfigConfigurer} is a helper class
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
 * </p>
 */
@Slf4j
public class DefaultConfigConfigurer {

    public static final String APPLICATION_CONFIG_PREFIX = "applicationConfig:";

    private final ConfigSourceDefaultConfigInserter configSourceInserter = new ConfigSourceDefaultConfigInserter();
    private final PropertySourceDefaultConfigInserter propertySourceInserter = new PropertySourceDefaultConfigInserter();

    private boolean isApplicationConfigName(String name) {
        return name.startsWith(APPLICATION_CONFIG_PREFIX);
    }

    private String getLastApplicationConfigName(ConfigurableEnvironment environment) {
        return StreamSupport.stream(environment.getPropertySources().spliterator(), false)
                .map(org.springframework.core.env.PropertySource::getName)
                .filter(this::isApplicationConfigName)
                .reduce((a, b) -> b)
                .orElse(null);
    }

    public void insertDefaultConfigs(ConfigurableEnvironment env, ConfigSource[] configSources) {
        insertDefaultConfigs(env, configSources, null);
    }

    protected void insertDefaultConfigs(ConfigurableEnvironment env,
                                        ConfigSource[] configSources,
                                        PropertySource[] propertySources) {
        String lastAppConfig = getLastApplicationConfigName(env);

        int before = env.getPropertySources().size();

        // we assume a higher precedence of ConfigSource than PropertySource
        insertPropertySources(env, lastAppConfig, propertySources);
        insertConfigFiles(env, lastAppConfig, configSources);

        log.info("added {} property sources as default config", env.getPropertySources().size() - before);
    }

    private void insertPropertySources(ConfigurableEnvironment env,
                                       String lastAppConfig,
                                       PropertySource[] propertySources) {
        if (propertySources != null) {
            propertySourceInserter.insertSources(env, lastAppConfig, propertySources);
        }
    }

    private void insertConfigFiles(ConfigurableEnvironment env,
                                   String lastAppConfig,
                                   ConfigSource[] configSources) {
        if (configSources != null) {
            configSourceInserter.insertSources(env, lastAppConfig, configSources);
        }
    }
}
