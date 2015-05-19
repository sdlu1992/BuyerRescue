package lu.shaode.netsupport;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import lu.shaode.netsupport.listener.ApiListener;

/**
 * Created by sdlu on 15/3/20.
 */
public class BizManager {

    private final String TAG = ((Object) this).getClass().getSimpleName();
    private static BizManager singleton;
    private Context context;
    private RequestQueue mQueue;

    public static synchronized BizManager getInstance(Context context){
        if (singleton == null){
            singleton = new BizManager(context);
        }
        return singleton;
    }

    public BizManager(Context context){
        this.context = context;
        mQueue = Volley.newRequestQueue(context);
    }

    public void register(String phone, String name, String pwd, String email, final ApiListener listener){
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("password", pwd);
        params.put("phone", phone);
        params.put("email", email);
        post(ApiConfig._REGISTER, params, listener);
    }

    public void login(String phone, String pwd, final ApiListener listener){
        Map<String, String> params = new HashMap<>();
        params.put("password", pwd);
        params.put("phone", phone);
        post(ApiConfig._LOGIN, params, listener);
    }

    public void getUserInfo(final ApiListener listener){
        Map<String, String> params = new HashMap<>();
        post(ApiConfig._USER_INFO, params, listener);
    }

    public void getCategory(final ApiListener listener){
        Map<String, String> params = new HashMap<>();
        get(ApiConfig._CATEGORY, params, listener);
    }

    public void getGoodsByCategory(String category, final ApiListener listener){
        Map<String, String> params = new HashMap<>();
        params.put("category", category);
        post(ApiConfig._GET_GOODS_BY_CATEGORY, params, listener);
    }

    public void getGoodsBySearch(String searchKey, final ApiListener listener){
        Map<String, String> params = new HashMap<>();
        params.put("search_key", searchKey);
        post(ApiConfig._GET_GOODS_BY_SEARCH, params, listener);
    }

    public void getGoodById(String id, final  ApiListener listener){
        Map<String, String> params = new HashMap<>();
        params.put("good_id", id);
        post(ApiConfig._GET_GOOD, params, listener);
    }

    public void addWishList(String good_id, String count, final  ApiListener listener){
        Map<String, String> params = new HashMap<>();
        params.put("good_id", good_id);
        params.put("count", count);
        post(ApiConfig._ADD_WISH, params, listener);
    }

    public void getWishList(final  ApiListener listener){
        Map<String, String> params = new HashMap<>();
        post(ApiConfig._GET_WISH_LIST, params, listener);
    }

    public void getHistoryList(final  ApiListener listener){
        Map<String, String> params = new HashMap<>();
        post(ApiConfig._GET_HISTORY_LIST, params, listener);
    }

    public void addOrder(JSONArray goods, final  ApiListener listener){
        Map<String, String> params = new HashMap<>();
        params.put("goods", goods.toString());
        post(ApiConfig._ORDER, params, listener);
    }

    public void addOrder(JSONArray goods, JSONArray wishes, final  ApiListener listener){
        Map<String, String> params = new HashMap<>();
        params.put("goods", goods.toString());
        params.put("wish_list", wishes.toString());
        post(ApiConfig._ORDER, params, listener);
    }

    public void getOrder(String orderId, final  ApiListener listener){
        Map<String, String> params = new HashMap<>();
        params.put("order_id", orderId);
        post(ApiConfig._GET_ORDER, params, listener);
    }

    public void pay(String orderId, String historyId, final  ApiListener listener){
        Map<String, String> params = new HashMap<>();
        params.put("order_id", orderId);
        params.put("history_id", historyId);
        post(ApiConfig._PAY, params, listener);
    }

    public void takeGoods(String orderId, String historyId, final  ApiListener listener){
        Map<String, String> params = new HashMap<>();
        params.put("order_id", orderId);
        params.put("history_id", historyId);
        post(ApiConfig._TAKE_GOODS, params, listener);
    }

    public void applyRefund(String orderId, String historyId, final  ApiListener listener){
        Map<String, String> params = new HashMap<>();
        params.put("order_id", orderId);
        params.put("history_id", historyId);
        post(ApiConfig._APPLY_REFUND, params, listener);
    }

    public void appraise(String orderId, String historyId,int type, String content, final  ApiListener listener){
        Map<String, String> params = new HashMap<>();
        params.put("history_id", historyId);
        params.put("order_id", orderId);
        params.put("type", String.valueOf(type));
        params.put("content", content);
        post(ApiConfig._APPRAISE, params, listener);
    }

    public void getAppraiseList(String goodId, final  ApiListener listener){
        Map<String, String> params = new HashMap<>();
        params.put("good_id", goodId);
        post(ApiConfig._APPRAISE_LIST, params, listener);
    }

    public void recharge(String money, final  ApiListener listener){
        Map<String, String> params = new HashMap<>();
        params.put("money", money);
        post(ApiConfig._RECHARGE, params, listener);
    }

    public void addCollect(String goodId, final  ApiListener listener){
        Map<String, String> params = new HashMap<>();
        params.put("good_id", goodId);
        post(ApiConfig._COLLECT, params, listener);
    }

    public void getCollectList(final  ApiListener listener){
        Map<String, String> params = new HashMap<>();
        post(ApiConfig._GET_COLLECT_LIST, params, listener);
    }

    public void getHome(final  ApiListener listener){
        Map<String, String> params = new HashMap<>();
        post(ApiConfig._GET_HOME, params, listener);
    }

    public void getAddressList(final  ApiListener listener){
        Map<String, String> params = new HashMap<>();
        post(ApiConfig._GET_ADDRESS_LIST, params, listener);
    }

    public void addAddress(String name, String phone, String zip, String province, String city, String county, String detail, final  ApiListener listener){
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("phone", phone);
        params.put("province", province);
        params.put("city", city);
        params.put("county", county);
        params.put("zip", zip);
        params.put("detail", detail);
        post(ApiConfig._ADD_ADDRESS, params, listener);
    }

    public void delAddress(String id, final  ApiListener listener){
        Map<String, String> params = new HashMap<>();
        params.put("address_id", id);
        post(ApiConfig._DEL_ADDRESS, params, listener);
    }

    public void post(String url, Map<String, String> params, final ApiListener listener){
        params.put("platform", "android");
        params.put("token", AppConfigCache.getCacheConfigString(context, "token"));
        Request<JSONObject> request = mQueue.add(new JsonObjectRequest(Request.Method.POST, url,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "response : " + response.toString());
                        listener.success(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG + "sdlu error.getMessage() = ", error.toString());
                listener.error(error.toString());
            }
        }));
        mQueue.start();
    }

    public void get(String url, Map<String, String> params, final ApiListener listener){
        params.put("platform", "android");
        params.put("token", AppConfigCache.getCacheConfigString(context, "token"));
        Request<JSONObject> request = mQueue.add(new JsonObjectRequest(Request.Method.GET, url,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "response : " + response.toString());
                        listener.success(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG + "sdlu error.getMessage() = ", error.toString());
                listener.error(error.toString());
            }
        }));
        mQueue.start();
    }
}
