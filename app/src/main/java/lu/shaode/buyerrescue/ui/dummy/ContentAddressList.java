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
public class ContentAddressList {

    public static List<Address> ITEMS = new ArrayList<>();

    public static Map<String, Address> ITEM_MAP = new HashMap<String, Address>();

    private static void addItem(Address item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static class Address {
        public String id;
        public String detail;
        public String name;
        public String phone;
        public String province;
        public String city;
        public String county;
        public String zip;
        public int isDefault;
        public JSONObject jsonObject;

        public Address(String id, String detail, String name, String phone, String province, String city, String county, String zip, int isDefault) {
            this.id = id;
            this.detail = detail;
            this.name = name;
            this.phone = phone;
            this.province = province;
            this.city = city;
            this.county = county;
            this.zip = zip;
            this.isDefault = isDefault;
        }

        public Address(JSONObject jsonObject){
            try {
                this.id = jsonObject.getString("id");
                this.detail = jsonObject.getString("detail");
                this.name = jsonObject.getString("name");
                this.phone = jsonObject.getString("phone");
                this.province = jsonObject.getString("province");
                this.city = jsonObject.getString("city");
                this.county = jsonObject.getString("county");
                this.zip = jsonObject.getString("zip");
                this.isDefault = jsonObject.getInt("is_default");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            this.jsonObject = jsonObject;
        }

        @Override
        public String toString() {
            return id;
        }
    }
}
