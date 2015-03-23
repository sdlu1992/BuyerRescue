package lu.shaode.netsupport;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

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
}
