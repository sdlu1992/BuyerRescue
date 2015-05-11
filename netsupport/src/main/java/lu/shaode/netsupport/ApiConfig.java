package lu.shaode.netsupport;

/**
 * Created by sdlu on 15/3/20.
 */
public class ApiConfig {

    public static final boolean isDebug = true;
    public static final String _DOMAIN_ROOT = isDebug ? "http://10.12.14.132:8098/" : "http://45.62.105.141:8000/";

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

    public static final String _APPLY_REFUND = _DOMAIN_ROOT
                                            + "applyRefund/";

    public static final String _APPRAISE = _DOMAIN_ROOT
                                            + "appraise/";

    public static final String _APPRAISE_LIST = _DOMAIN_ROOT
                                            + "getAppraiseList/";

    public static final String _RECHARGE    = _DOMAIN_ROOT
                                            + "recharge/";

    public static final String _COLLECT     = _DOMAIN_ROOT
                                            + "collect/";

    public static final String _GET_COLLECT_LIST = _DOMAIN_ROOT
                                            + "getCollectList/";

    public static final String _GET_HOME    = _DOMAIN_ROOT
                                            + "getHome/";
}
