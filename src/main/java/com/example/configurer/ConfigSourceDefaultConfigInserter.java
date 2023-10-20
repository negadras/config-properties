package com.example.configurer;

import com.example.ConfigSource;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Inserts config source files provided by {@link ConfigSource} annotations as default config.
 *
 * 
 * @since 2018-07-26
 */
public class ConfigSourceDefaultConfigInserter extends AbstractDefaultConfigInserter<ConfigSource> {

    private final List<PropertySourceLoader> propertySourceLoaders;

    public ConfigSourceDefaultConfigInserter() {
        this.propertySourceLoaders = SpringFactoriesLoader.loadFactories(
                PropertySourceLoader.class,
                getClass().getClassLoader()
        );
    }

    @Override
    public void insertSource(ConfigurableEnvironment env, String lastAppConfig, ConfigSource configSource) {
        String name = configSource.name();
        if (!StringUtils.hasLength(name)) {
            name = null;
        }
        String encoding = configSource.encoding();
        if (!StringUtils.hasLength(encoding)) {
            encoding = null;
        }

        String[] locations = configSource.value();
        // reversed to insert them in correct order
        // 1st entry should be evaluated first (be closed the app config than following ones)
        for (int i = locations.length - 1; i >= 0; i--) {
            insert(name, locations[i], encoding, env, lastAppConfig);
        }
    }

    @Override
    protected List<org.springframework.core.env.PropertySource<?>> getPropertySource(String name, String location, String encoding) {
        Optional<PropertySourceLoader> loaderOptional = getLoader(location);
        if (loaderOptional.isEmpty()) {
            return Collections.emptyList();
        }

        PropertySourceLoader loader = loaderOptional.get();

        try {
            return loader.load(
                    name,
                    getResource(location, encoding).getResource()
            );

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Optional<PropertySourceLoader> getLoader(String location) {
        String extension = location.substring(location.lastIndexOf('.') + 1);

        return propertySourceLoaders.stream()
                .filter(loader ->
                                Stream.of(loader.getFileExtensions())
                                        .anyMatch(supported -> supported.equals(extension))
                )
                .findFirst();
    }
}

