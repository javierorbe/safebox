package deusto.safebox.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class ItemTypeTest {

    @Test
    void differentIdsTest() {
        // Supplier of a stream with all the ItemType IDs.
        Supplier<IntStream> idStream = () -> Arrays.stream(ItemType.values()).mapToInt(ItemType::getId);
        // Assert that there are no repeated IDs.
        assertEquals(
                idStream.get().distinct().count(),
                idStream.get().count(),
                "There is more than one ItemType with the same ID."
        );
    }

    @Test
    void fromIdTest() {
        Arrays.stream(ItemType.values())
                .forEach(type -> assertEquals(
                        type,
                        ItemType.fromId(type.getId()),
                        "ItemType.fromId() doesn't return the correct ItemType for the value " + type.getId()
                ));
    }
}
