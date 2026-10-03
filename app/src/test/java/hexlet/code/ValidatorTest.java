package hexlet.code;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import hexlet.code.schemas.StringSchema;
import org.junit.jupiter.api.Test;

class ValidatorTest {

    @Test
    void testFactoryCreatesIndependentSchemas() {
        Validator validator = new Validator();
        StringSchema first = validator.string().required();
        StringSchema second = validator.string();

        assertNotSame(first, second);
        assertFalse(first.isValid(""));
        assertTrue(second.isValid(""));
    }

    @Test
    void testFactoryCreatesEachSchemaType() {
        Validator validator = new Validator();

        assertTrue(validator.string().isValid(null));
        assertTrue(validator.number().isValid(null));
        assertTrue(validator.map().isValid(null));
    }
}
