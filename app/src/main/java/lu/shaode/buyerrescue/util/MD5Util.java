package lu.shaode.buyerrescue.util;

import java.security.MessageDigest;

/**
 * Created by sdlu on 15/4/12.
 */
public class MD5Util {

    public static String MD5(String s) {
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            StringBuffer sb = new StringBuffer();
            for (byte aMd : md) {
                int val = ((int) aMd) & 0xff;
                if (val < 16)
                    sb.append("0");
                sb.append(Integer.toHexString(val));

            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
