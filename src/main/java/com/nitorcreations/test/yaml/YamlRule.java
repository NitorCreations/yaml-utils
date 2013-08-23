package com.nitorcreations.test.yaml;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.BeanAccess;

/**
 * Populates the fields marked with {@link com.nitorcreations.test.yaml.FromYaml} annotation
 * from the target {@code testClass}'s resource named "{@link Class#getSimpleName()}.yaml"
 * <p/>
 * NOTE: If using maven, be sure to add testResources from src/test/java to copy the yaml resources
 * from test sources folder to {@code target/test-classes}
 *
 * @see <a href="https://code.google.com/p/snakeyaml/">Snakeyaml website</a>
 */
public class YamlRule implements TestRule {

    public static final String YAML_EXTENSION = ".yaml";

    private final Object testClass;
    private String fileName;
    private Constructor constructor = new Constructor();
    private BeanAccess beanAccess = BeanAccess.DEFAULT;

    private YamlRule(Object testClass) {
        Validate.notNull(testClass, "Test class");
        this.testClass = testClass;
        this.fileName = testClass.getClass().getSimpleName() + YAML_EXTENSION;
    }

    /**
     * Create a new YamlRule with the filename set to {@link Class#getSimpleName()}.
     * @param testClass
     * @return
     */
    public static YamlRule forClass(Object testClass) {
        return new YamlRule(testClass);
    }

    /**
     * Change the name of the file to be loaded. The
     * file will be loaded with {@link Class#getResourceAsStream(String)}
     *
     * @param fileName the new file name to be loaded.
     * @return this, for chaining
     */
    public YamlRule withFile(String fileName) {
        this.fileName = fileName;
        return this;
    }

    /**
     * Change the Yaml's constructor instance to, e.g., add implicit tag
     * resolvers or custom constructs.
     * @param constructor the constructor to set
     * @return this, for chaining
     */
    public YamlRule withConstructor(Constructor constructor) {
        this.constructor = constructor;
        return this;
    }

    /**
     * Set the access to field access. The default is property access, i.e., getter/setter.
     * @return this, for chaining
     */
    public YamlRule withFieldAccess() {
        this.beanAccess = BeanAccess.FIELD;
        return this;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new YamlStatement(base);
    }

    protected InputStream getResource() {
        return testClass.getClass().getResourceAsStream(fileName);
    }

    public Constructor getConstructor() {
        return constructor;
    }

    protected Yaml getYaml() {
        Yaml yaml = new Yaml(constructor);
        yaml.setBeanAccess(beanAccess);
        return yaml;
    }

    private class YamlStatement extends Statement {

        private final Statement base;

        public YamlStatement(Statement base) {
            this.base = base;
        }

        @Override
        public void evaluate() throws Throwable {
            final Map<String, Object> objectMap = readObjects();
            Class<?> targetClass = testClass.getClass();
            while (targetClass != Object.class) {
                for (Field field : targetClass.getDeclaredFields()) {
                    if (field.isAnnotationPresent(FromYaml.class)) {
                        Object o = objectMap.get(field.getName());
                        checkCorrespondingObjectInYaml(field, o);
                        field.setAccessible(true);
                        field.set(testClass, o);
                    }
                }
                targetClass = targetClass.getSuperclass();
            }
            base.evaluate();
        }

        @SuppressWarnings("unchecked")
        private Map<String, Object> readObjects() {
            final InputStream resource = getResource();
            Validate.notNull(resource, String.format("Resource '%s' should not be null", fileName));
            return (Map<String, Object>) getYaml().load(resource);
        }
    }

    private void checkCorrespondingObjectInYaml(Field field, Object o) {
        if (o == null) {
            throw new IllegalArgumentException(String.format(
                    "Encountered field '%s' with no corresponding object in the yaml: '%s'",
                    field.getName(),
                    fileName
            ));
        }
    }

}
