package com.example.loader;


import com.example.ConfigSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.springframework.util.StringUtils.commaDelimitedListToStringArray;

/**
 * Basis for config loaders which wish to load config source files by their key from {@code META-INF/config.loaders}.
 *
 * 
 * @since 2018-07-26
 */
public abstract class AbstractConfigLoader
        implements ConfigLoader {

    @Override
    public ConfigSource[] getConfigSources() {
        List<String> locations = getLocations();
        if (locations.isEmpty()) {
            return new ConfigSource[0];
        }

        ConfigSource configSource = new ConfigSourceImpl(
                getClass().getName(),
                locations.toArray(new String[locations.size()]),
                "UTF-8"
        );

        return new ConfigSource[]{configSource};
    }

    protected List<String> getLocations() {
        List<String> locations = new ArrayList<>();
        try {
            Enumeration<URL> resourceURLs = getClass().getClassLoader()
                    .getResources(LOADERS_RESOURCE_LOCATION);

            while (resourceURLs.hasMoreElements()) {
                URL url = resourceURLs.nextElement();

                locations.addAll(getLocations(url));
            }

            return locations;

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private List<String> getLocations(URL resourceURL) throws IOException {
        UrlResource springFactories = new UrlResource(resourceURL);
        if (!springFactories.exists()) {
            return emptyList();
        }

        try (InputStream inputStream = springFactories.getInputStream()) {
            Properties properties = new Properties();
            properties.load(inputStream);

            String config = properties.getProperty(getClass().getName());
            return asList(commaDelimitedListToStringArray(config));
        }
    }


    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    @Accessors(fluent = true)
    @SuppressWarnings("ClassExplicitlyAnnotation")
    private static class ConfigSourceImpl implements ConfigSource {

        private final String name;
        private final String[] value;
        private String encoding;

        @Override
        public Class<? extends Annotation> annotationType() {
            return ConfigSource.class;
        }
    }
}
