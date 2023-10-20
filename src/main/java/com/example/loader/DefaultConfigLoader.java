package com.example.loader;


import com.example.ConfigSource;
import com.example.configurer.DefaultConfigConfigurer;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Loads configs files provided at {@code META-INF/config.loaders} with key being this class name
 * as default configuration (inserted after application configuration).
 */
public class DefaultConfigLoader extends AbstractConfigLoader implements ConfigLoader, Ordered {

    public static final int DEFAULT_ORDER = 0;

    private final DefaultConfigConfigurer configurer = new DefaultConfigConfigurer();

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    @Override
    public void insertDefaultConfigs(ConfigurableEnvironment env, ConfigSource[] configSources) {
        configurer.insertDefaultConfigs(env, configSources);
    }
}
