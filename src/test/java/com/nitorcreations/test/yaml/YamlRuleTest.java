package com.nitorcreations.test.yaml;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class YamlRuleTest {

    public static class NormalCase {
        @Rule
        public YamlRule yamlRule = YamlRule.forClass(this);

        @FromYaml
        private String stringFromYaml;

        @FromYaml
        private static String overrideFromYaml = "My value";

        @FromYaml
        private List<String> list;

        // The name exists in the yaml, but not annotated
        private String notFromYaml = null;

        @FromYaml
        private TestObject customObject;

        @FromYaml
        private TestObject customObjectWithConstructor;

        @Test
        public void allFieldsPopulatedCorrectly() {
            assertThat(stringFromYaml, is("The string from Yaml"));
            assertThat(overrideFromYaml, is("This is the override"));
            assertThat(list, contains("A", "B", "C"));
            assertThat(notFromYaml, nullValue());
            assertThat(customObject, hasProperty("value", is("Test setter")));
            assertThat(customObjectWithConstructor, hasProperty("value", is("Foobar")));
        }
    }

    public static class ChangedParameters {
        @Rule
        public YamlRule yamlRule = YamlRule.forClass(this)
                .withFile("AnotherCase.yaml")
                .withConstructor(new TestConstructor())
                .withFieldAccess();

        @FromYaml
        private TestObject constructedCorrectly = null;

        @FromYaml
        private TestObject fieldAccess = null;

        @Test
        public void fieldsPopulatedCorrectly() {
            assertThat(constructedCorrectly, allOf(
                    hasProperty("value", is("Barbabapa")),
                    hasProperty("fieldWithNoSetter", is("Bar"))
            ));
            assertThat(fieldAccess, hasProperty("fieldWithNoSetter", is("This field is set!")));
        }
    }

    protected static class TestConstructor extends Constructor {
        public TestConstructor() {
            yamlConstructors.put(new Tag("!to"), new TestObjectConstructor());
        }

        private class TestObjectConstructor extends ConstructScalar {
            @Override
            public Object construct(Node nnode) {
                return new TestObject(((ScalarNode)nnode).getValue());
            }
        }
    }

    protected static class TestObject {
        private String value;
        private String fieldWithNoSetter = "Bar";

        public TestObject() {}
        public TestObject(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getFieldWithNoSetter() {
            return fieldWithNoSetter;
        }
    }
}
