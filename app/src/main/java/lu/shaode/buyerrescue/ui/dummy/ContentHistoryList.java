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
public class ContentHistoryList {

    public static List<History> ITEMS = new ArrayList<>();

    public static Map<String, History> ITEM_MAP = new HashMap<String, History>();

    private static void addItem(History item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static class History {
        public String id;
        public ContentGoods.Good good;
        public String date;
        public int count;
        public String order_id;
        public int state;
        public boolean isCheck = true;
        public String price;
        public ContentAppraiseList.Appraise appraise = null;

        public History(String id, ContentGoods.Good good, String date, int count, String order_id, int state, String price) {
            this.id = id;
            this.good = good;
            this.date = date;
            this.count = count;
            this.order_id = order_id;
            this.state = state;
            this.price = price;
        }

        public History(JSONObject jsonObject){
            try {
                this.id = jsonObject.getString("id");
                this.count = jsonObject.getInt("amount");
                this.date = jsonObject.getString("date");
                this.order_id = jsonObject.getString("order");
                this.state = jsonObject.getInt("state");
                this.price = jsonObject.getString("price");
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

        public ContentAppraiseList.Appraise getAppraise() {
            return appraise;
        }

        public void setAppraise(ContentAppraiseList.Appraise appraise) {
            this.appraise = appraise;
        }

        @Override
        public String toString() {
            return id;
        }
    }
}
