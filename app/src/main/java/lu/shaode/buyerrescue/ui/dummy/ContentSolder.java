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
public class ContentSolder {

    public static List<Solder> ITEMS = new ArrayList<Solder>();

    public static Map<String, Solder> ITEM_MAP = new HashMap<String, Solder>();

    private static void addItem(Solder item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static class Solder {
        public String id;
        public String phone;
        public String name;
        public String email;
        public String store;
        public String address;

        public Solder(String id, String phone, String name, String email) {
            this.id = id;
            this.phone = phone;
            this.name = name;
            this.email = email;
        }

        public Solder(JSONObject jsonSolder){
            try {
                id = jsonSolder.getString("id");
                phone = jsonSolder.getString("phone");
                name = jsonSolder.getString("name");
                email = jsonSolder.getString("email");
                address = jsonSolder.getString("address");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public String toString() {
            return id;
        }
    }
}
