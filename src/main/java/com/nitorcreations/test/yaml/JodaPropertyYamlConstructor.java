package com.nitorcreations.test.yaml;

import org.apache.commons.lang3.Validate;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;

/**
 * Add support for the following constructs for Joda-Time:
 * <ul>
 *     <li>{@code !ld &lt;input&gt;} &rarr; {@code LocalDate.parse(&lt;input&gt;)}</li>
 *     <li>{@code !lt &lt;input&gt;} &rarr; {@code LocalTime.parse(&lt;input&gt;)}</li>
 *     <li>{@code !dt &lt;input&gt;} &rarr; {@code DateTime.parse(&lt;input&gt;)}</li>
 * </ul>
 */
public class JodaPropertyYamlConstructor extends Constructor {
    public JodaPropertyYamlConstructor() {
        yamlConstructors.put(new Tag("!ld"), new LocalDateConstructor());
        yamlConstructors.put(new Tag("!lt"), new LocalTimeConstructor());
        yamlConstructors.put(new Tag("!dt"), new DateTimeConstructor());
    }

    protected class LocalDateConstructor extends Constructor.ConstructScalar {
        @Override
        public Object construct(Node nnode) {
            Validate.isAssignableFrom(ScalarNode.class, nnode.getClass());
            return LocalDate.parse(((ScalarNode) nnode).getValue());
        }
    }

    protected class DateTimeConstructor extends Constructor.ConstructScalar {
        @Override
        public Object construct(Node nnode) {
            Validate.isAssignableFrom(ScalarNode.class, nnode.getClass());
            return DateTime.parse(((ScalarNode) nnode).getValue());
        }
    }

    protected class LocalTimeConstructor extends Constructor.ConstructScalar {
        @Override
        public Object construct(Node nnode) {
            Validate.isAssignableFrom(ScalarNode.class, nnode.getClass());
            return LocalTime.parse(((ScalarNode) nnode).getValue());
        }
    }
}
