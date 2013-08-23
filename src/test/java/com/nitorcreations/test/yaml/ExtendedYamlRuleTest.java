package com.nitorcreations.test.yaml;

import org.junit.Test;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertThat;

public class ExtendedYamlRuleTest {

    @Test
    public void hasCorrectConstructor() {
        assertThat(ExtendedYamlRule.withJoda(this),
                hasProperty("constructor", instanceOf(JodaPropertyYamlConstructor.class)));
    }

}
