package com.example;

import java.lang.annotation.*;

/**
 * Similar to {@link org.springframework.context.annotation.PropertySource},
 * but with support for any config source file, not just properties files.
 *
 * @since 2018-07-25
 * @see org.springframework.context.annotation.PropertySource
 * @see ConfigSources
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(ConfigSources.class)
public @interface ConfigSource {

    String name() default "";

    String[] value();

    /**
     * A specific character encoding for the given resources, e.g. "UTF-8".
     * @since 4.3
     */
    String encoding() default "";
}
