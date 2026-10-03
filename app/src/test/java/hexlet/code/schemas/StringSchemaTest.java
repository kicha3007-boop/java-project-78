package hexlet.code.schemas;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import hexlet.code.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class StringSchemaTest {

    private StringSchema schema;

    @BeforeEach
    void setUp() {
        schema = new Validator().string();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testEmptyIsValidWhenNotRequired(String value) {
        assertTrue(schema.isValid(value));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testEmptyIsInvalidWhenRequired(String value) {
        assertFalse(schema.required().isValid(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"what does the fox say", "hexlet", " "})
    void testRequiredAcceptsNonEmpty(String value) {
        assertTrue(schema.required().isValid(value));
    }

    @Test
    void testRulesSkipEmptyValueWhenNotRequired() {
        assertTrue(schema.minLength(5).contains("hex").isValid(""));
        assertTrue(schema.isValid(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"wh", "what", "fox say", "what does the fox say"})
    void testContainsAccepts(String substring) {
        assertTrue(schema.contains(substring).isValid("what does the fox say"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"whatthe", "Fox", "dog"})
    void testContainsRejects(String substring) {
        assertFalse(schema.contains(substring).isValid("what does the fox say"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Hexle", "Hexlet", "Hexlet!"})
    void testMinLengthAccepts(String value) {
        assertTrue(schema.minLength(5).isValid(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"H", "Hexl"})
    void testMinLengthRejects(String value) {
        assertFalse(schema.minLength(5).isValid(value));
    }

    @Test
    void testRepeatedRuleReplacesPrevious() {
        assertTrue(schema.minLength(10).minLength(4).isValid("Hexlet"));

        schema.contains("what");
        assertTrue(schema.isValid("what does the fox say"));
        schema.contains("whatthe");
        assertFalse(schema.isValid("what does the fox say"));
    }

    @Test
    void testDifferentRulesWorkTogether() {
        schema.required().minLength(5).contains("hex");

        assertTrue(schema.isValid("hexlet"));
        assertFalse(schema.isValid("hex"));
        assertFalse(schema.isValid("Hexlet"));
        assertFalse(schema.isValid(""));
    }
}
