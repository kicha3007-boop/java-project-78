package hexlet.code.schemas;

import java.util.Map;

public final class MapSchema extends BaseSchema<Map<?, ?>> {

    public MapSchema required() {
        markRequired();
        return this;
    }

    public MapSchema sizeof(int size) {
        addCheck("sizeof", value -> value.size() == size);
        return this;
    }

    /** Значение каждого ключа проверяется своей схемой — той же, что и для отдельных значений. */
    public <T> MapSchema shape(Map<String, BaseSchema<T>> schemas) {
        addCheck(
                "shape",
                value ->
                        schemas.entrySet().stream()
                                .allMatch(
                                        entry ->
                                                isValidValue(
                                                        entry.getValue(),
                                                        value.get(entry.getKey()))));
        return this;
    }

    // Тип значений проверяемой мапы задаёт вызывающий код через схемы в shape()
    @SuppressWarnings("unchecked")
    private static <T> boolean isValidValue(BaseSchema<T> schema, Object value) {
        return schema.isValid((T) value);
    }
}
