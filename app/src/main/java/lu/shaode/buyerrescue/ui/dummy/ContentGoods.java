package lu.shaode.buyerrescue.ui.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ContentGoods {

    /**
     * An array of sample (dummy) items.
     */
    public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    static {
        // Add 3 sample items.
        addItem(new DummyItem("1", "Item 1", "mygoods", 10+"", "2", "mystore", 30+"", "11"));
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public String name;
        public String count;
        public String price;
        public String describe;
        public String store;
        public String storeName;
        public String id;
        public String category;

        public DummyItem(String id, String describe, String name, String price, String store, String storeName, String count, String category) {
            this.id = id;
            this.describe = describe;
            this.name = name;
            this.price = price;
            this.store = store;
            this.storeName = storeName;
            this.count = count;
        }

        @Override
        public String toString() {
            return describe;
        }
    }
}
