package inventory;

import com.badlogic.gdx.utils.Array;
import com.foloke.starcorp.packer.ContainerType;
import com.foloke.starcorp.Inventory.Inventory;
import com.foloke.starcorp.Inventory.Item;
import com.foloke.starcorp.packer.PItem;
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

        PItem testItemData = new PItem(1, 100, ContainerType.SOLID);

        assertFalse(inventory.couldTake(new Item(testItemData, 3)));
        assertTrue(inventory.couldTake(new Item(testItemData, 3), 1));

        Array<Item> items = new Array<>();
        PItem testItemData1 = new PItem(2, item1Volume, ContainerType.SOLID);
        PItem testItemData2 = new PItem(3, item2Volume, ContainerType.SOLID);

        for(int i = 0; i < itemsCount; i++) {
            items.add(new Item(testItemData1, 1));
            items.add(new Item(testItemData2, 1));
        }

        assertTrue(inventory.couldTake(items));

        PItem testItemData3 = new PItem(4, 10, ContainerType.SOLID);
        for(int i = 0; i < itemsCount; i++) {
            items.add(new Item(testItemData3, 1));
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

        PItem testItemData1 = new PItem(1, item1Volume, ContainerType.SOLID);
        PItem testItemData2 = new PItem(2, item2Volume, ContainerType.SOLID);

        for(int i = 0; i < items1Count; i++) {
            assertTrue(inventory.addItem(new Item(testItemData1, 1)));
        }

        assertEquals(item1Volume * items1Count, inventory.getVolume());

        Array<Item> items = new Array<>();
        for(int i = 0; i < items2Count; i++) {
            items.add(new Item(testItemData2, 1));
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

        PItem testItemData3 = new PItem(5, 0, ContainerType.GAS);
        assertFalse(inventory.couldTake(new Item(testItemData3, 1)));
    }

    @Test
    public void removeTest() {
        addTest();

        int items1Count = 5;
        int items2Count = 7;
        float item1Volume = 15;
        float item2Volume = 21;

        PItem testItemData1 = new PItem(1, item1Volume, ContainerType.SOLID);
        PItem testItemData2 = new PItem(2, item2Volume, ContainerType.SOLID);

        for(int i = 0; i < items1Count; i++) {
            inventory.addItem(new Item(testItemData1, 1));
        }

        Array<Item> items = new Array<>();
        for(int i = 0; i < items2Count; i++) {
            items.add(new Item(testItemData2, 1));
        }
    }
}
