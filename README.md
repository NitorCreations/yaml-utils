yaml-utils
==========

Currently, the package contains only a single JUnit rule that provides a way to easily initialize graphs of domain objects for unit testing purposes. Example below. 

The `YamlRule` loads the test class's class resource as the Yaml resource and uses the [Snakeyaml](http://snakeyaml.org) library to parse the results. The `YamlRule` can be configured:

- `withFile(String fileName)` - Use a different class resource. Uses `Class#getSimpleName() + ".yaml"` by default
- `withConstructor(Constructor)` - Specify a Snakeyaml constructor for custom easy constructs. See the example in the [Snakeyaml documentation](https://code.google.com/p/snakeyaml/wiki/Documentation#Constructors,_representers,_resolvers)
- `withFieldAccess()` - Change to field access instead of property access (setter/getter)

### Example usage
#### FooTest.yaml
	field: !!com.example.FooTest$TestObject &testObject
      value: This works!
    list:
      - *testObject
      - !!com.example.FooTest$TestObject [ "And this is a constructor" ]
#### FooTest.java
    public class FooTest {
        @Rule
        public YamlRule yamlRule = YamlRule.forClass(this);
        
        @FromYaml
        private TestObject field;
        
        @FromTYaml
        private List<TestObject> list;
        
        @Test
        public void testFoo() {
        	assertThat(field, hasProperty("value", is("This works!")));    
        }
        
        @Test
        public void testList() {
        	assertThat(list, contains(
            	sameInstance(field),
                hasProperty("value", is("And this is a constructor"))
            ));
        }
        
        public static class TestObject {
            private String value;
            public TestObject() {}
            public TestObject(value) { this.value = value; }
            // getters and setters omitted for brevity
        }
    }

### Maven
If you are using maven, be sure to copy the resources beside the class files to `test-classes` directory:

	<build>
    	...
        <testResources>
        	<testResource>
                <filtering>false</filtering>
                <directory>src/test/resources</directory>
            </testResource>
        </testResources>
        ...
    </build>
