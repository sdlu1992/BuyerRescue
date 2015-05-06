package lu.shaode.netsupport;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import org.json.JSONException;
import org.json.JSONObject;

public class AppConfigCache {

    private static final String APP_CONFIG = "app_config";
    
    public static String getCacheConfigString(Context ctx, String name) {
        SharedPreferences sp = ctx.getSharedPreferences(APP_CONFIG, Context.MODE_PRIVATE);
        return sp.getString(name, "");
    }

    public static void setCacheConfig(Context ctx, String name, String value) {
        SharedPreferences sp = ctx.getSharedPreferences(APP_CONFIG, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(name, value);
        editor.commit();
    }

    public static int getCacheConfigInt(Context ctx, String name) {
        SharedPreferences sp = ctx.getSharedPreferences(APP_CONFIG, Context.MODE_PRIVATE);
        return sp.getInt(name, -1);
    }

    public static void setCacheConfig(Context ctx, String name, int value) {
        SharedPreferences sp = ctx.getSharedPreferences(APP_CONFIG, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt(name, value);
        editor.commit();
    }

    public static long getCacheConfigLong(Context ctx, String name) {
        SharedPreferences sp = ctx.getSharedPreferences(APP_CONFIG, Context.MODE_PRIVATE);
        return sp.getLong(name, -1L);
    }

    public static void setCacheConfig(Context ctx, String name, long value) {
        SharedPreferences sp = ctx.getSharedPreferences(APP_CONFIG, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putLong(name, value);
        editor.commit();
    }

    public static void saveLoginInfo(Context ctx, String token, String name, String phone, String email){
        AppConfigCache.setCacheConfig(ctx, "token", token);
        AppConfigCache.setCacheConfig(ctx, "name", name);
        AppConfigCache.setCacheConfig(ctx, "phone", phone);
        AppConfigCache.setCacheConfig(ctx, "email", email);

    }

    public static void saveLoginInfo(Context ctx, JSONObject info){
        try {
            AppConfigCache.setCacheConfig(ctx, "token", info.getString("token"));
            AppConfigCache.setCacheConfig(ctx, "name", info.getString("name"));
            AppConfigCache.setCacheConfig(ctx, "phone", info.getString("phone"));
            AppConfigCache.setCacheConfig(ctx, "email", info.getString("email"));
            AppConfigCache.setCacheConfig(ctx, "money", info.getString("money"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void logout(Context ctx){
        AppConfigCache.setCacheConfig(ctx, "token", "");
        AppConfigCache.setCacheConfig(ctx, "name", "");
        AppConfigCache.setCacheConfig(ctx, "phone", "");
        AppConfigCache.setCacheConfig(ctx, "email", "");

    }
}
