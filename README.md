# props-config
This module provides you with features to

- setup your libraries default configuration
- create your own config loader mechanism and insertion logic

**Default configuration** hereby is a configuration which is always 
  overridden by your application configuration.


## Register default config files
In order to register config files this module provides you with 2 ways to that:

1. `META-INF/config.loaders`
2. extending `AbstractAnnotationDefaultConfigConfigurer` and registering the files by using annotations
   (`@ConfigSources`, `@ConfigSource`, `@PropertySources`, `@PropertySource`)

### META-INF/config.loaders
The syntax of `config.loaders` is similar to `spring.factories` the standard properties file syntax
and expects the loader class name as key and a comma separated list of config file paths as argument.

Generic example:
```
the.config.loader.ClassName=\
    example/config-file.properties,\
    example/config-file.yaml
```

Example using the provided default loader:
```
com.example.loader.DefaultConfigLoader=\
    example/auto-loaded/config-file.properties,\
    example/auto-loaded/config-file.yaml
```

## Annotation based setup
In general, this module supports the standard annotations `@PropertySource` and its
container annotation `@PropertySources` as well as `@ConfigSource` and its container annotation `@ConfigSources`.

**Attention**:
The short-comings of `@PropertySource` are that it **only supports `*.properties` files** and will not fail
in case you provide any other file type like Yaml. It just tries to render it as properties file.
This might result in wrong config key-value pairs.

You can also find examples on the annotation usage in the [tests](src/test/java/org/example/app/config/).

```java
@ConfigSources({
        @ConfigSource(
                name = "my-config-file-name",
                value = {
                    "classpath:/example/config-files.yaml"
                }
        ),
        // ...
})
@PropertySources({
        @PropertySource(
                name = "my-property-source-name",
                value = {
                        "classpath:/example/property-source.properties",
                }
        ),
        // ...
})
public class ConfigFilesDefaultConfig extends AbstractAnnotationDefaultConfigConfigurer {
}
```

Be aware of that that `@ConfigSource` files will be ordered **before** `@PropertySource` files, have a higher precedence.

In general, we would discourage the use of `@PropertySource` and encourage the use of `@ConfigSource`,
but due to the general usage of it, you might still want to consider to use them.
In those cases, just be aware of its limitations.


## Create your own config loaders
All functionality is provided in reusable blocks.

You can

- extend `AbstractAnnotationDefaultConfigConfigurer`:
  usually used for the annotation based setup, but you can modify any part you want as well
- use or extend `DefaultConfigConfigurer`:
  e.g. have your own way of loading config and use this to insert it as "default config", ...
- extend `DefaultConfigLoader`:
  e.g. add conditional loading / filtering capabilities as of your needs, ...
- extend `AbstractConfigLoader`:
  reuse the loading mechanism (described above) for your own loader
  (e.g. loader to override config properties to enforce them to have a certain value
  which cannot be overridden by application configs, ...)
- implement `ConfigLoader`:
   provide your own loading mechanism and how you want to add configs
