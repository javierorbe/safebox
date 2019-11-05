package deusto.safebox.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.function.Consumer;
import org.junit.jupiter.api.Test;

class ClassConsumerMapTest {

    @Test
    void test() {
        ClassConsumerMap map = new ClassConsumerMap();

        Consumer<Integer> integerOperation = integer -> {};
        Class<Integer> integerClass = Integer.class;
        map.put(integerClass, integerOperation);

        map.get(integerClass).ifPresentOrElse(
            operation -> {
                assertEquals(integerOperation, operation);
                operation.accept(2);
            },
            () -> fail("There is no operation defined for " + integerClass.getName())
        );
    }
}
