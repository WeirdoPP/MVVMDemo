package google.architecture.coremodel.http.service.config;


/**
 * @author Jewel
 * @version 1.0
 * @since 2017/7/20 0020
 */

public class APIConstant {

    public static final String GANK_HOST = "http://gank.io/";

    public static final String NET_CODE_SUCCESS = "1";
    public static final String NET_CODE_ERROR = "0";
    public static final String NET_CODE_NEED_LOGIN = "9999";  // token过期重新登录
    public static final String NET_CODE_ORDER_STATE_SUCCEED = "3901";  // 取消订单时，订单状态为支付完成
    public static final String NET_CODE_PAY_AMOUNT_HAVE_CHANGE = "3902";  // 订单结账时，订单优惠金额有误
    public static final String NET_CODE_ORDER_CHANGE = "3903";  //操作订单时,该订单正在被其他人修改
    public static final String NET_CODE_NOT_CONNECT = "-1";  // 网络连接异常

    public static final String NET_CODE_CONNECT = "400";
    public static final String NET_CODE_UNKNOWN_HOST = "401";
    public static final String NET_CODE_SOCKET_TIMEOUT = "402";
    public static final String NET_CODE_SERVICE_ERROR = "service_error";

    public static final String CONNECT_EXCEPTION = "网络连接异常，请检查您的网络状态";
    public static final String SOCKET_TIMEOUT_EXCEPTION = "网络连接超时，请检查您的网络状态，稍后重试";
    public static final String UNKNOWN_HOST_EXCEPTION = "网络异常，请检查您的网络状态";
    public static final String SERVICE_EXCEPTION = "服务异常，请重试";


}
