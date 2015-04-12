package lu.shaode.buyerrescue.util;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by sdlu on 15/4/12.
 */
public class BuyerApplication extends Application{

    public static RequestQueue queue = null;

    @Override
    public void onCreate() {
        super.onCreate();
        BuyerImageCache.getInstance().initilize(this);
        if (queue == null){
            queue = Volley.newRequestQueue(getApplicationContext());
        }
    }
}
