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
public class ContentWishList {

    public static List<Wish> ITEMS = new ArrayList<Wish>();

    public static Map<String, Wish> ITEM_MAP = new HashMap<String, Wish>();

    private static void addItem(Wish item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static class Wish {
        public String id;
        public ContentGoods.Good good;
        public String date;
        public int count;
        public boolean isCheck = true;

        public Wish(String id, ContentGoods.Good good, String date, int count) {
            this.id = id;
            this.good = good;
            this.date = date;
            this.count = count;
        }

        public Wish(JSONObject jsonObject){
            try {
                this.id = jsonObject.getString("id");
                this.count = jsonObject.getInt("amount");
                this.date = jsonObject.getString("date");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void setGood(ContentGoods.Good good) {
            this.good = good;
        }

        public void setCheck(boolean isCheck) {
            this.isCheck = isCheck;
        }

        public boolean isCheck() {
            return isCheck;
        }

        @Override
        public String toString() {
            return id;
        }
    }
}
