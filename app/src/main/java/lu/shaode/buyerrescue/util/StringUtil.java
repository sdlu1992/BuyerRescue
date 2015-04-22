package lu.shaode.buyerrescue.util;

import java.text.DecimalFormat;

/**
 * Created by sdlu on 15/4/17.
 */
public class StringUtil {

    public static String getMoneyString(float money){
        DecimalFormat decimalFormat=new DecimalFormat(".00");
        return decimalFormat.format(money);
    }

    public static String getMoneyString(String money){
        float m = Float.parseFloat(money);
        return getMoneyString(m);
    }

    public static boolean isNullOrEmpty(String string){
        return string == null || string.equals("");
    }
}
