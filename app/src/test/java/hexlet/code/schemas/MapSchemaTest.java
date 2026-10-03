package hexlet.code.schemas;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import hexlet.code.Validator;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MapSchemaTest {

    private Validator validator;
    private MapSchema schema;

    @BeforeEach
    void setUp() {
        validator = new Validator();
        schema = validator.map();
    }

    private static Map<String, String> human(String firstName, String lastName) {
        Map<String, String> human = new HashMap<>();
        human.put("firstName", firstName);
        human.put("lastName", lastName);
        return human;
    }

    @Test
    void testNullIsValidWhenNotRequired() {
        assertTrue(schema.isValid(null));
        assertTrue(schema.sizeof(2).isValid(null));
    }

    @Test
    void testRequired() {
        schema.required();

        assertFalse(schema.isValid(null));
        assertTrue(schema.isValid(new HashMap<>()));
        assertTrue(schema.isValid(Map.of("key1", "value1")));
    }

    @Test
    void testSizeof() {
        Map<String, String> data = new HashMap<>();
        data.put("key1", "value1");
        schema.required().sizeof(2);

        assertFalse(schema.isValid(data));
        data.put("key2", "value2");
        assertTrue(schema.isValid(data));
        data.put("key3", "value3");
        assertFalse(schema.isValid(data));
    }

    @Test
    void testRepeatedSizeofReplacesPrevious() {
        schema.sizeof(1).sizeof(2);

        assertTrue(schema.isValid(Map.of("a", 1, "b", 2)));
        assertFalse(schema.isValid(Map.of("a", 1)));
    }

    @Test
    void testShapeWithStrings() {
        Map<String, BaseSchema<String>> schemas = new HashMap<>();
        schemas.put("firstName", validator.string().required());
        schemas.put("lastName", validator.string().required().minLength(2));
        schema.shape(schemas);

        assertTrue(schema.isValid(human("John", "Smith")));
        assertFalse(schema.isValid(human("John", null)));
        assertFalse(schema.isValid(human("Anna", "B")));
        assertFalse(schema.isValid(human("", "Smith")));
    }

    @Test
    void testShapeWithNumbers() {
        Map<String, BaseSchema<Integer>> schemas = new HashMap<>();
        schemas.put("age", validator.number().required().positive());
        schemas.put("floor", validator.number().range(1, 9));
        schema.shape(schemas);

        Map<String, Integer> flat = new HashMap<>();
        flat.put("age", 30);
        assertTrue(schema.isValid(flat));
        flat.put("floor", 10);
        assertFalse(schema.isValid(flat));
        flat.put("floor", 9);
        flat.put("age", 0);
        assertFalse(schema.isValid(flat));
    }

    @Test
    void testShapeWorksWithOtherRules() {
        Map<String, BaseSchema<String>> schemas = new HashMap<>();
        schemas.put("firstName", validator.string().required());
        schema.required().sizeof(2).shape(schemas);

        assertTrue(schema.isValid(human("John", null)));
        assertFalse(schema.isValid(Map.of("firstName", "John")));
        assertFalse(schema.isValid(human(null, "Smith")));
    }
}
