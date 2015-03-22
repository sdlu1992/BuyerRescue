package lu.shaode.netsupport.listener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sdlu on 15/3/20.
 */
public interface ApiListener {
    public void success(JSONObject jsonObject);
    public void error(String string);
}
