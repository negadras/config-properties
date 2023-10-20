package com.example.configurer;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

import static java.util.Collections.singletonList;

/**
 * Inserts config source files provided by {@link PropertySource} annotations as default config.
 *
 * 
 * @since 2018-07-26
 */
public class PropertySourceDefaultConfigInserter extends AbstractDefaultConfigInserter<PropertySource> {

    private final PropertySourceFactory factory = new DefaultPropertySourceFactory();

    @Override
    public void insertSource(ConfigurableEnvironment env, String lastAppConfig, PropertySource source) {
        String name = source.name();
        if (!StringUtils.hasLength(name)) {
            name = null;
        }
        String encoding = source.encoding();
        if (!StringUtils.hasLength(encoding)) {
            encoding = null;
        }

        String[] locations = source.value();
        // reversed to insert them in correct order
        // 1st entry should be evaluated first (be closed the app config than following ones)
        for (int i = locations.length - 1; i >= 0; i--) {
            insert(name, locations[i], encoding, env, lastAppConfig);
        }
    }

    @Override
    protected List<org.springframework.core.env.PropertySource<?>> getPropertySource(String name, String location, String encoding) {
        try {
            org.springframework.core.env.PropertySource<?> source =
                    factory.createPropertySource(name, getResource(location, encoding));

            return singletonList(source);

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
