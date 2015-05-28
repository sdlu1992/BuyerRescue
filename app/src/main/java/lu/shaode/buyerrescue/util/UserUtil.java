package lu.shaode.buyerrescue.util;

import android.content.Context;

import lu.shaode.netsupport.AppConfigCache;

/**
 * Created by sdlu on 15/5/24.
 */
public class UserUtil {

    public static boolean isLogin(Context context){
        return !StringUtil.isNullOrEmpty(AppConfigCache.getCacheConfigString(context, "token"));
    }
}
