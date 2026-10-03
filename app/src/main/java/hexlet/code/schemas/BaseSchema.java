package hexlet.code.schemas;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Общая часть всех схем: правила по имени и запуск проверки.
 *
 * @param <T> тип проверяемого значения
 */
public abstract class BaseSchema<T> {

    private final Map<String, Predicate<T>> checks = new LinkedHashMap<>();
    private boolean isRequired;

    /** Правило с тем же именем заменяет прежнее, а не добавляется вторым. */
    protected final void addCheck(String name, Predicate<T> check) {
        checks.put(name, check);
    }

    protected final void markRequired() {
        isRequired = true;
    }

    /** Отсутствующее значение: без required() оно валидно, и правила к нему не применяются. */
    protected boolean isEmpty(T value) {
        return value == null;
    }

    public final boolean isValid(T value) {
        if (isEmpty(value)) {
            return !isRequired;
        }
        return checks.values().stream().allMatch(check -> check.test(value));
    }
}
