package google.architecture.coremodel.http.service.core.interceptor;

import com.apkfuns.logutils.LogUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * @author Jewel
 * @version 1.0
 * @since 2017/6/14 0014
 * <p>
 * 定义请求头拦截器
 */
// FIXME: 2018/9/3 修复请求头
public class HeaderInterceptor implements Interceptor {

    public HeaderInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        RequestBody body = originalRequest.body();
        long bodyLength = body == null ? 0 : body.contentLength();
        LogUtils.d("接口请求参数长度为:%s", bodyLength);
        String method = originalRequest.url().toString();
        LogUtils.d("接口请求地址为:%s", method);

        //这里实现请求头
//        Map<String, String> headers = BaseApplication.getInstance().getRequestHeader(method, bodyLength);
//
//        //如果公共请求头不为空,则构建新的请求
//        if (!headers.isEmpty()) {
//            Request.Builder requestBuilder = originalRequest.newBuilder();
//            requestBuilder.header(APIKey.HEAD_CONTENT_ENCODING, "UTF-8");
//            requestBuilder.header(APIKey.HEAD_CONTENT_TYPE, "application/json");
//
//            for (String key : headers.keySet()) {
//                if(!TextUtils.isEmpty(headers.get(key))) {
//                    requestBuilder.addHeader(key, headers.get(key));  // 头部flag字段默认为Application配置的
//                }
//            }
//            requestBuilder.method(originalRequest.method(), originalRequest.body());
//
//            Request newRequest = requestBuilder.build();
//            LogUtils.d("HEADER => \n%s", newRequest.headers().toString());
//            return chain.proceed(newRequest);
//        }

        return chain.proceed(originalRequest);
    }
}
