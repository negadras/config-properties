package com.example.loader;

import com.example.ConfigSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Interface for any kind of config loader implementation like {@link DefaultConfigLoader}.
 *
 * @see DefaultConfigLoader
 */
public interface ConfigLoader extends EnvironmentPostProcessor {

    String LOADERS_RESOURCE_LOCATION = "META-INF/config.loaders";

    @Override
    default void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        insertDefaultConfigs(environment, getConfigSources());
    }

    void insertDefaultConfigs(ConfigurableEnvironment env, ConfigSource[] configSources);

    ConfigSource[] getConfigSources();
}
