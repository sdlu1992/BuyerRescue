package lu.shaode.buyerrescue.ui.dummy;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 */
public class ContentGoods {

    public static List<Good> ITEMS = new ArrayList<Good>();

    public static Map<String, Good> ITEM_MAP = new HashMap<String, Good>();

    private static void addItem(Good item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static class Good {
        public String name;
        public String count;
        public String price;
        public String describe;
        public ContentStore.Store store;
        public String id;
        public String category;

        public Good(String id, String describe, String name, String price,
                    ContentStore.Store store, String count, String category) {
            this.id = id;
            this.describe = describe;
            this.name = name;
            this.price = price;
            this.store = store;
            this.count = count;
            this.category = category;
        }

        @Override
        public String toString() {
            return describe;
        }
    }
}
