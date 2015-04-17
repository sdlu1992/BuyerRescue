package lu.shaode.buyerrescue.ui.dummy;

import org.json.JSONException;
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
public class ContentStore {

    public static List<Store> ITEMS = new ArrayList<Store>();

    public static Map<String, Store> ITEM_MAP = new HashMap<String, Store>();

    private static void addItem(Store item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static class Store {
        public String id;
        public String name;
        public String credit;
        public ContentSolder.Solder solder;
        public String address;

        public Store(String id, String name, String credit, ContentSolder.Solder solder) {
            this.id = id;
            this.name = name;
            this.credit = credit;
            this.solder = solder;
        }

        public Store(JSONObject jsonObject){
            try {
                this.id = jsonObject.getString("id");
                this.name = jsonObject.getString("name");
                this.address = jsonObject.getString("address");
                this.credit = jsonObject.getString("credit");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void setSolder(ContentSolder.Solder solder) {
            this.solder = solder;
        }

        @Override
        public String toString() {
            return id;
        }
    }
}
