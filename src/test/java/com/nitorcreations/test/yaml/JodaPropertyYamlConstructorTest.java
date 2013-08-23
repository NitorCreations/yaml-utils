package com.nitorcreations.test.yaml;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import static org.junit.Assert.assertEquals;

public class JodaPropertyYamlConstructorTest {

    private Yaml yaml = new Yaml(new JodaPropertyYamlConstructor());

    @Test
    public void conversionToLocalDate() {
        assertEquals("String '!ld 2015-03-02 not converted to new LocalDate(2015, 3, 2);",
                new LocalDate(2015, 3, 2), yaml.load("!ld 2015-03-02"));
    }

    @Test
    public void conversionToLocalTime() {
        assertEquals("String '!lt 12:34 not converted to new LocalDate(12, 34);",
                new LocalTime(12, 34), yaml.load("!lt 12:34"));
    }

    @Test
    public void conversionToDateTime() {
        assertEquals("String '!dt 2011-10-09T08:07 not converted to new DateTime(2011, 10, 9, 8, 7, 0);",
                new DateTime(2011, 10, 9, 8, 7, 0), yaml.load("!dt 2011-10-09T08:07"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void failConversionOnEmpty_localDate() {
        yaml.load("!ld");
    }

    @Test(expected = IllegalArgumentException.class)
    public void failConversionOnEmpty_localTime() {
        yaml.load("!lt");
    }

    @Test(expected = IllegalArgumentException.class)
    public void failConversionOnEmpty_dateTime() {
        yaml.load("!dt");
    }

}
