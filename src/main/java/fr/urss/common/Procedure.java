package fr.urss.common;

import java.util.Objects;

@FunctionalInterface
public interface Procedure {
    void run();

    default Procedure andThen(Procedure after) {
        Objects.requireNonNull(after);
        return () -> {
            run();
            after.run();
        };
    }

    default Procedure compose(Procedure before) {
        Objects.requireNonNull(before);
        return () -> {
            before.run();
            run();
        };
    }
}