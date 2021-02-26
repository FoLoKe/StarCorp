package inventory;

import com.badlogic.gdx.utils.Array;
import com.foloke.starcorp.Inventory.ContainerType;
import com.foloke.starcorp.Inventory.Inventory;
import com.foloke.starcorp.Inventory.Item;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {
    static Inventory inventory;
    static int initialCapacity;

    @BeforeEach
    public void init() {
        initialCapacity = 100;
        inventory = new Inventory(initialCapacity, ContainerType.SOLID);
    }

    @Test
    public void capacityTest() {
        int targetMaxCapacity = 250;
        int targetEmpty = 0;

        assertEquals(targetEmpty, inventory.getVolume());
        assertEquals(initialCapacity, inventory.getMaxVolume());

        inventory.setMaxCapacity(targetMaxCapacity);
        assertEquals(targetEmpty, inventory.getVolume());
        assertEquals(targetMaxCapacity, inventory.getMaxVolume());
    }

    @Test
    public void checkTest() {
        int itemsCount = 10;
        float item1Volume = 5;
        float item2Volume = 4;

        assertFalse(inventory.couldTake(new Item(1, 100, 3, ContainerType.SOLID)));
        assertTrue(inventory.couldTake(new Item(1, 100, 3, ContainerType.SOLID), 1));

        Array<Item> items = new Array<>();
        for(int i = 0; i < itemsCount; i++) {
            items.add(new Item(2, item1Volume, 1, ContainerType.SOLID));
            items.add(new Item(3, item2Volume, 1, ContainerType.SOLID));
        }

        assertTrue(inventory.couldTake(items));

        for(int i = 0; i < itemsCount; i++) {
            items.add(new Item(4, 10, 1, ContainerType.SOLID));
        }

        assertFalse(inventory.couldTake(items));
    }

    @Test
    public void addTest() {
        int items1Count = 5;
        int items2Count = 7;
        float item1Volume = 15;
        float item2Volume = 21;
        float targetVolume = items1Count * item1Volume + items2Count * item2Volume;

        for(int i = 0; i < items1Count; i++) {
            assertTrue(inventory.addItem(new Item(1, item1Volume, 1, ContainerType.SOLID)));
        }

        assertEquals(item1Volume * items1Count, inventory.getVolume());

        Array<Item> items = new Array<>();
        for(int i = 0; i < items2Count; i++) {
            items.add(new Item(2, item2Volume, 1, ContainerType.SOLID));
        }

        assertFalse(inventory.addItems(items));

        assertNotEquals(targetVolume, inventory.getVolume());

        int actualCount2 = items2Count - Math.round((targetVolume - initialCapacity) / item2Volume);
        float actualVolume = items1Count * item1Volume + actualCount2 * item2Volume;

        assertEquals(actualVolume, inventory.getVolume());
        assertEquals(Math.round((targetVolume - initialCapacity) / item2Volume), items.size);

        assertEquals(2, inventory.getItems().size);
        assertEquals(items1Count, inventory.getItems().get(0).getCount());
        assertEquals(actualCount2, inventory.getItems().get(1).getCount());

        assertTrue(inventory.setMaxCapacity(actualVolume));
        assertTrue(inventory.setMaxCapacity(120));
        assertFalse(inventory.setMaxCapacity(actualVolume - 1));

        assertFalse(inventory.couldTake(new Item(5, 0, 1, ContainerType.GAS)));
    }

    @Test
    public void removeTest() {
        addTest();

        int items1Count = 5;
        int items2Count = 7;
        float item1Volume = 15;
        float item2Volume = 21;

        for(int i = 0; i < items1Count; i++) {
            inventory.addItem(new Item(1, item1Volume, 1, ContainerType.SOLID));
        }

        Array<Item> items = new Array<>();
        for(int i = 0; i < items2Count; i++) {
            items.add(new Item(2, item2Volume, 1, ContainerType.SOLID));
        }
    }
}
