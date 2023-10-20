package com.example.configurer;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.EncodedResource;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

/**
 * Abstract inserter which inserts config sources as default config (after the application config).
 *
 * 
 * @since 2018-07-26
 */
public abstract class AbstractDefaultConfigInserter<T> {

    protected final ResourceLoader resourceLoader = new DefaultResourceLoader(getClass().getClassLoader());

    @SafeVarargs
    public final void insertSources(ConfigurableEnvironment env,
                                    String lastAppConfig,
                                    T... configSources) {
        // reversed to insert them in correct order
        // 1st entry should be evaluated first (be closed the app config than following ones)
        for (int i = configSources.length - 1; i >= 0; i--) {
            insertSource(env, lastAppConfig, configSources[i]);
        }
    }

    public abstract void insertSource(ConfigurableEnvironment env, String lastAppConfig, T source);

    protected void insert(String name, String location, String encoding, ConfigurableEnvironment env, String lastAppConfig) {
        List<PropertySource<?>> sources =
                getPropertySource(toLocalizedName(name, location), location, encoding);
        if (sources == null || sources.isEmpty()) {
            return;
        }

        // ensure only eligible config sources based on the current environment config
        // as well as the correct order
        List<PropertySource<?>> orderedSources = filterAndOrderByActiveProfiles(sources, env);

        String relativeSourceName = lastAppConfig != null ? lastAppConfig : "random";

        // reversed to insert them in correct order
        // 1st entry should be evaluated first (be closed the app config than following ones)
        for (int i = orderedSources.size() - 1; i >= 0; i--) {
            env.getPropertySources()
                    .addAfter(relativeSourceName, orderedSources.get(i));
        }
    }

    private List<PropertySource<?>> filterAndOrderByActiveProfiles(List<PropertySource<?>> sources, ConfigurableEnvironment env) {
        return sources.stream()
                .filter(source -> isUsableConfigSource(source, env))
                .sorted((PropertySource<?> source1, PropertySource<?> source2) -> {
                    boolean isDefault1 = isDefaultConfigSource(source1);
                    boolean isDefault2 = isDefaultConfigSource(source2);

                    if (isDefault1 == isDefault2) {
                        return 0;
                    }

                    if (!isDefault1) {
                        return -1;
                    }

                    return 1;
                })
                .collect(toList());
    }

    private boolean isUsableConfigSource(PropertySource<?> source, ConfigurableEnvironment env) {
        if (isDefaultConfigSource(source)) {
            return true;
        }

        Object profiles = source.getProperty("spring.profiles");
        requireNonNull(profiles);

        String[] assignedProfiles = profiles
                .toString()
                .split("\\s*,\\s*");

        List<String> activeProfiles = asList(env.getActiveProfiles());

        return Stream.of(assignedProfiles)
                .anyMatch(activeProfiles::contains);
    }

    private boolean isDefaultConfigSource(PropertySource<?> source) {
        Object profiles = source.getProperty("spring.profiles");
        if (isNull(profiles)) {
            // is applicable for all profiles
            return true;
        }

        String profilesStr = profiles.toString();
        return profilesStr.isEmpty();
    }

    protected abstract List<org.springframework.core.env.PropertySource<?>> getPropertySource(String name, String location, String encoding);

    protected EncodedResource getResource(String location, String encoding) {
        return new EncodedResource(resourceLoader.getResource(location), encoding);
    }

    protected String toLocalizedName(String name, String location) {
        return String.format("%s [%s]", name, location);
    }
}
