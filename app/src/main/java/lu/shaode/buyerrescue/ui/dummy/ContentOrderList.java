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
public class ContentOrderList {

    public static List<Order> ITEMS = new ArrayList<Order>();

    public static Map<String, Order> ITEM_MAP = new HashMap<String, Order>();

    private static void addItem(Order item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static class Order {
        public String id;
        public ArrayList<ContentHistoryList.History> histories;
        public String date;
        public ContentAddressList.Address address;

        public Order(String id, String date) {
            this.id = id;
            this.date = date;
            histories = new ArrayList<>();
        }

        public Order(String id, ArrayList<ContentHistoryList.History> histories, String date) {
            this.id = id;
            this.date = date;
            this.histories = histories;
        }

        public Order(JSONObject jsonObject){
            histories = new ArrayList<>();
            try {
                this.id = jsonObject.getString("id");
                this.date = jsonObject.getString("date");
                this.address = new ContentAddressList.Address(jsonObject.getJSONObject("address"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void setHistories(ArrayList<ContentHistoryList.History> histories) {
            this.histories = histories;
        }

        public ArrayList<ContentHistoryList.History> getHistories() {
            return histories;
        }

        public void addHistory(ContentHistoryList.History history){
            if (histories != null){
                histories.add(history);
            }
        }

        @Override
        public String toString() {
            return id;
        }
    }
}
