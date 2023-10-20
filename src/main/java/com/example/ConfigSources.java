package com.example;

import java.lang.annotation.*;

/**
 * Container annotation that aggregates several {@link ConfigSource} annotations.
 *
 * <p>Can be used natively, declaring several nested {@link ConfigSource} annotations.
 * Can also be used in conjunction with Java 8's support for <em>repeatable annotations</em>,
 * where {@link ConfigSource} can simply be declared several times on the same
 * {@linkplain ElementType#TYPE type}, implicitly generating this container annotation.
 *
 * 
 * @since 2018-07-25
 * @see ConfigSource
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConfigSources {

    ConfigSource[] value();
}
