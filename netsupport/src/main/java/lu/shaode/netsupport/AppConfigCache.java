package lu.shaode.netsupport;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        editor.apply();
    }

    public static int getCacheConfigInt(Context ctx, String name) {
        SharedPreferences sp = ctx.getSharedPreferences(APP_CONFIG, Context.MODE_PRIVATE);
        return sp.getInt(name, -1);
    }

    public static void setCacheConfig(Context ctx, String name, int value) {
        SharedPreferences sp = ctx.getSharedPreferences(APP_CONFIG, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt(name, value);
        editor.apply();
    }

    public static long getCacheConfigLong(Context ctx, String name) {
        SharedPreferences sp = ctx.getSharedPreferences(APP_CONFIG, Context.MODE_PRIVATE);
        return sp.getLong(name, -1L);
    }

    public static void setCacheConfig(Context ctx, String name, long value) {
        SharedPreferences sp = ctx.getSharedPreferences(APP_CONFIG, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putLong(name, value);
        editor.apply();
    }

    public static Set<String> getCacheConfigStringSet(Context ctx, String name) {
        SharedPreferences sp = ctx.getSharedPreferences(APP_CONFIG, Context.MODE_PRIVATE);
        return sp.getStringSet(name, new HashSet<String>());
    }

    public static void setCacheConfig(Context ctx, String name, Set<String> value) {
        SharedPreferences sp = ctx.getSharedPreferences(APP_CONFIG, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putStringSet(name, value);
        editor.apply();
    }

    public static void addCacheConfigStringSet(Context ctx, String name, String value) {
        SharedPreferences sp = ctx.getSharedPreferences(APP_CONFIG, Context.MODE_PRIVATE);
        Set<String> stringSet = new HashSet<>();
        stringSet.addAll(sp.getStringSet(name, new HashSet<String>()));
        Editor editor = sp.edit();
        stringSet.add(value);
        editor.putStringSet(name, stringSet);
        editor.apply();
    }

    public static void saveLoginInfo(Context ctx, String token, String name, String phone, String email){
        AppConfigCache.setCacheConfig(ctx, "token", token);
        AppConfigCache.setCacheConfig(ctx, "name", name);
        AppConfigCache.setCacheConfig(ctx, "phone", phone);
        AppConfigCache.setCacheConfig(ctx, "email", email);

    }

    public static void setStringArray(Context ctx, String name, List<String> list) {
        SharedPreferences sp = ctx.getSharedPreferences(APP_CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEdit1= sp.edit();
        mEdit1.putInt(name + "_size",list.size()); /*sKey is an array*/
        for(int i=0;i<list.size();i++) {
            mEdit1.putString(name + i, list.get(i));
        }
        mEdit1.apply();
    }

    public static List<String> getStringArray(Context ctx, String name) {
        SharedPreferences sp = ctx.getSharedPreferences(APP_CONFIG, Context.MODE_PRIVATE);
        List<String> list = new ArrayList<>();
        list.clear();
        int size = sp.getInt(name + "_size", 0);
        for(int i=0;i<size;i++) {
            list.add(sp.getString(name + i, null));
        }
        return list;
    }

    public static void addSearchArrayString(Context ctx, String name, String value){
        List<String> strings = getStringArray(ctx, name);
        strings.remove(value);
        strings.add(value);
        while (strings.size() > 9){
            strings.remove(0);
        }
        setStringArray(ctx, name, strings);
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
