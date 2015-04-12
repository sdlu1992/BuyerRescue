package lu.shaode.netsupport;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

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

    public void register(Map<String, String> params, final ApiListener listener){
        post(ApiConfig._REGISTER, params, listener);
    }

    public void login(Map<String, String> params, final ApiListener listener){
        post(ApiConfig._LOGIN, params, listener);
    }

    public void getUserInfo(Map<String, String> params, final ApiListener listener){
        post(ApiConfig._USER_INFO, params, listener);
    }

    public void getCategory(Map<String, String> params, final ApiListener listener){
        get(ApiConfig._CATEGORY, params, listener);
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
