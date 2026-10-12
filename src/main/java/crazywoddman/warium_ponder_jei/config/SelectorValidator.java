package crazywoddman.warium_ponder_jei.config;

import java.util.function.Predicate;

public record SelectorValidator(String... values) implements Predicate<Object> {

    @Override
    public boolean test(Object value) {
        for (String allowed : values)
            if (allowed.equals(value))
                return true;

        return false;
    }
}