package lu.shaode.netsupport;

/**
 * Created by sdlu on 15/3/20.
 */
public class ApiConfig {

    public static final boolean isDebug = true;
    public static final String _DOMAIN_ROOT = isDebug ? "http://192.168.2.25:9887/" : "http://120.26.64.234:8000/";

    public static final String _REGISTER    = _DOMAIN_ROOT
                                            + "register/";

    public static final String _LOGIN       = _DOMAIN_ROOT
                                            + "login/";

    public static final String _USER_INFO   = _DOMAIN_ROOT
                                            + "info/";

    public static final String _CATEGORY    = _DOMAIN_ROOT
                                            + "category/";
}
