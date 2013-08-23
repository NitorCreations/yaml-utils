package com.nitorcreations.test.yaml;

public final class ExtendedYamlRule {

    /**
     * Create a {@link YamlRule} with a {@link JodaPropertyYamlConstructor}.
     * Adds support to Joda-Time.
     *
     * @param testClass the class under test
     * @return the rule
     * @see YamlRule#forClass(Object)
     * @see JodaPropertyYamlConstructor
     */
    public static YamlRule withJoda(Object testClass) {
        return YamlRule.forClass(testClass).withConstructor(new JodaPropertyYamlConstructor());
    }
}
