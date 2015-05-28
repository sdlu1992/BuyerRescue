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
public class ContentCollectList {

    public static List<Collect> ITEMS = new ArrayList<>();

    public static Map<String, Collect> ITEM_MAP = new HashMap<String, Collect>();

    private static void addItem(Collect item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static class Collect {
        public String id;
        public ContentGoods.Good good;
        public int isCollect;

        public Collect(String id, ContentGoods.Good good, int isCollect) {
            this.id = id;
            this.good = good;
            this.isCollect = isCollect;
        }

        public Collect(JSONObject jsonObject){
            try {
                this.id = jsonObject.getString("id");
                this.isCollect = jsonObject.getInt("is_collect");
                good = new ContentGoods.Good(jsonObject.getJSONObject("good"));
                ContentStore.Store store = new ContentStore.Store(jsonObject.getJSONObject("store"));
                good.setStore(store);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void setGood(ContentGoods.Good good) {
            this.good = good;
        }

        @Override
        public String toString() {
            return id;
        }
    }
}
