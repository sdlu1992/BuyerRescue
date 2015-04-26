package lu.shaode.buyerrescue.ui.dummy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lu.shaode.netsupport.ApiConfig;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 */
public class ContentGoods {

    public static List<Good> ITEMS = new ArrayList<>();

    public static Map<String, Good> ITEM_MAP = new HashMap<>();

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
        public String imageUrlTitle;
        public ArrayList<String> imageUrlOther;

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

        public Good(JSONObject jsonObject) {
            try {
                this.id = jsonObject.getString("id");
                this.describe = jsonObject.getString("des");
                this.name = jsonObject.getString("name");
                this.price = jsonObject.getString("price");
                this.category = jsonObject.getString("category");
                this.imageUrlTitle= ApiConfig._DOMAIN_ROOT + jsonObject.getString("image_url_title");
                imageUrlOther = new ArrayList<>();
                for (int i = 1; i<5; i++){
                    String image = jsonObject.getString("image"+i);
                    if (!image.equals("")){
                        imageUrlOther.add(ApiConfig._DOMAIN_ROOT + image);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void setStore(ContentStore.Store store) {
            this.store = store;
        }

        @Override
        public String toString() {
            return describe;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }
}
