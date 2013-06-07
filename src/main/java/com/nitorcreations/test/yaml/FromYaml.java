package com.nitorcreations.test.yaml;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks the test classes field to be populated from the yaml resource given for a certain {@link
 * com.nitorcreations.test.yaml.YamlRule}
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FromYaml {
}
