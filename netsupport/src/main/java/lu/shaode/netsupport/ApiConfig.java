package lu.shaode.netsupport;

/**
 * Created by sdlu on 15/3/20.
 */
public class ApiConfig {

    public static final boolean isDebug = false;
    public static final String _DOMAIN_ROOT = isDebug ? "http://192.168.2.25:9887/" : "http://45.62.105.141:8000/";

    public static final String _REGISTER    = _DOMAIN_ROOT
                                            + "register/";

    public static final String _LOGIN       = _DOMAIN_ROOT
                                            + "login/";

    public static final String _USER_INFO   = _DOMAIN_ROOT
                                            + "info/";

    public static final String _CATEGORY    = _DOMAIN_ROOT
                                            + "category/";

    public static final String _GET_GOODS_BY_CATEGORY   = _DOMAIN_ROOT
                                            + "getGoodsByCategory/";

    public static final String _GET_GOOD    = _DOMAIN_ROOT
                                            + "good/";

    public static final String _ADD_WISH    = _DOMAIN_ROOT
                                            + "addWishList/";

    public static final String _GET_WISH_LIST = _DOMAIN_ROOT
                                            + "getWishList/";

    public static final String _ORDER       = _DOMAIN_ROOT
                                            + "order/";

    public static final String _GET_HISTORY_LIST = _DOMAIN_ROOT
                                            + "getBuyHistory/";

    public static final String _GET_ORDER   = _DOMAIN_ROOT
                                            + "getOrder/";

    public static final String _PAY         = _DOMAIN_ROOT
                                            + "payForGoods/";

    public static final String _TAKE_GOODS  = _DOMAIN_ROOT
                                            + "takeGoods/";
}
