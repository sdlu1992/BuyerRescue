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
public class ContentAppraiseList {

    public static List<Appraise> ITEMS = new ArrayList<>();

    public static Map<String, Appraise> ITEM_MAP = new HashMap<String, Appraise>();

    private static void addItem(Appraise item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static class Appraise {
        public String id;
        public String date;
        public String content;
        public int type;
        public String username;

        public Appraise(String id, String date, String content, int type, String username) {
            this.id = id;
            this.date = date;
            this.content = content;
            this.type = type;
            this.username = username;
        }

        public Appraise(JSONObject jsonObject){
            try {
                this.id = jsonObject.getString("id");
                this.date = jsonObject.getString("date");
                this.content = jsonObject.getString("content");
                this.type = jsonObject.getInt("type");
                this.username = jsonObject.getString("username");
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
