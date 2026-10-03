package hexlet.code.schemas;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import hexlet.code.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class NumberSchemaTest {

    private NumberSchema schema;

    @BeforeEach
    void setUp() {
        schema = new Validator().number();
    }

    @Test
    void testNullIsValidWhenNotRequired() {
        assertTrue(schema.isValid(null));
        assertTrue(schema.positive().range(1, 2).isValid(null));
    }

    @Test
    void testRequired() {
        schema.required();

        assertFalse(schema.isValid(null));
        assertTrue(schema.isValid(0));
        assertTrue(schema.isValid(-10));
        assertTrue(schema.isValid(10));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, Integer.MAX_VALUE})
    void testPositiveAccepts(int value) {
        assertTrue(schema.positive().isValid(value));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10, Integer.MIN_VALUE})
    void testPositiveRejects(int value) {
        assertFalse(schema.positive().isValid(value));
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 7, 10})
    void testRangeIncludesBounds(int value) {
        assertTrue(schema.range(5, 10).isValid(value));
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 11, -7})
    void testRangeRejectsOutside(int value) {
        assertFalse(schema.range(5, 10).isValid(value));
    }

    @Test
    void testRepeatedRangeReplacesPrevious() {
        schema.range(5, 10).range(6, 9);

        assertFalse(schema.isValid(5));
        assertFalse(schema.isValid(10));
        assertTrue(schema.isValid(6));
        assertTrue(schema.isValid(9));
    }

    @Test
    void testDifferentRulesWorkTogether() {
        schema.required().positive().range(-5, 5);

        assertTrue(schema.isValid(5));
        assertFalse(schema.isValid(-3));
        assertFalse(schema.isValid(0));
        assertFalse(schema.isValid(6));
    }
}
