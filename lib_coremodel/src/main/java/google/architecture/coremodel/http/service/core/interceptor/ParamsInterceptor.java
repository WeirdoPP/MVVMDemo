package google.architecture.coremodel.http.service.core.interceptor;

import com.apkfuns.logutils.LogUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * ╭╯☆★☆★╭╯
 * 　　╰╮★☆★╭╯
 * 　　　 ╯☆╭─╯
 * 　　 ╭ ╭╯
 * 　╔╝★╚╗  ★☆╮     BUG兄，没时间了快上车    ╭☆★
 * 　║★☆★║╔═══╗　╔═══╗　╔═══╗  ╔═══╗
 * 　║☆★☆║║★　☆║　║★　☆║　║★　☆║  ║★　☆║
 * ◢◎══◎╚╝◎═◎╝═╚◎═◎╝═╚◎═◎╝═╚◎═◎╝..........
 *
 * @author Jewel
 * @version 1.0
 * @since 2017/6/14 0014
 * <p>
 * 定义请求参数拦截器
 */
public class ParamsInterceptor implements Interceptor {

    /**
     * 获取FormBody表单的请求参数
     *
     * @param request 原有请求
     * @return 表单请求参数列表
     */
    private static Map<String, String> getFormParams(Request request) {
        Map<String, String> params = null;
        FormBody body = null;
        try {
            body = (FormBody) request.body();
        } catch (ClassCastException e) {
            LogUtils.e("获取表单参数失败：%s", e);
        }
        if (body != null) {
            int size = body.size();
            if (size > 0) {
                params = new HashMap<>();
                for (int i = 0; i < size; i++) {
                    params.put(body.name(i), body.value(i));
                    LogUtils.d("请求参数%1$s为：%2$s", body.name(i), body.value(i));
                }
            }
        }
        return params;
    }

//    /**
//     * 根据请求头处理get请求的参数data
//     *
//     * @param httpUrlBuilder HttpUrl.Builder构建对象
//     * @param headFlag       请求头flag字段值
//     * @param paramData      请求参数原data字段值
//     */
//    private void handleGetParams(HttpUrl.Builder httpUrlBuilder, String headFlag, String paramData) {
//        if (TextUtils.equals(headFlag, ENCRYPT_3DS.getValue())) {
//            httpUrlBuilder.setQueryParameter(APIKey.PARAM_DATA, des3Encode(paramData));
//        } else if (TextUtils.equals(headFlag, ENCRYPT_SYMMETRIC.getValue())) {
//
//        }
//    }

//    /**
//     * 根据请求头处理post请求的表单字段data
//     * @param headFlag  请求头flag字段值
//     * @param paramData 请求参数原data字段值
//     * @return 处理后的data字段值
//     */
//    private String handPostParams(String headFlag, String paramData) {
//        String encodeParam = paramData;
//        if (TextUtils.equals(headFlag, ENCRYPT_3DS.getValue())) {
//            encodeParam = des3Encode(paramData);
//        }
//        return encodeParam;
//    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        if (originalRequest.body() == null) {
            return chain.proceed(originalRequest);
        }

//        String method = originalRequest.method();
//        Timber.d("请求方式g为：%s", method);
//
//        String headFlag = originalRequest.header(APIKey.HEAD_FLAG);
//        Timber.d("请求头flag为：%s", headFlag);
//
//        if(TextUtils.isEmpty(headFlag) || TextUtils.equals(headFlag, APIConstant.HEADER_FLAG_ENUM.ENCRYPT_NONE.getValue())) {
//            return chain.proceed(originalRequest);
//        }
//
//        Request.Builder newRequestBuilder = originalRequest.newBuilder();
//        //GET请求则使用HttpUrl.Builder来构建
//        if ("GET".equalsIgnoreCase(method)) {
//            Timber.d("执行get请求逻辑构建新的请求");
//            HttpUrl.Builder httpUrlBuilder = originalRequest.url().newBuilder();
//            // 获取请求参数data
//            String paramData = originalRequest.url().queryParameter(APIKey.PARAM_DATA);
//            // 根据请求头处理data
//            handleGetParams(httpUrlBuilder, headFlag, paramData);
//            // 构建新的请求
//            newRequestBuilder.url(httpUrlBuilder.build());
//        } else {
//            Timber.d("执行非get请求逻辑构建新的请求");
//            //如果原请求是表单请求
//            if (originalRequest.body() instanceof FormBody) {
//                Timber.d("执行FormBody请求逻辑开始");
//                FormBody.Builder formBodyBuilder = new FormBody.Builder();
//                Iterator<Map.Entry<String, String>> entryIterator = getFormParams(originalRequest).entrySet().iterator();
//                while (entryIterator.hasNext()) {
//                    Map.Entry<String, String> entry = entryIterator.next();
//                    String key = entry.getKey();
//                    String value = entry.getValue();
//                    Timber.d("原请求参数%1$s为：%2$s", key, value);
//                    String encodeValue = handPostParams(headFlag, value);
//                    Timber.d("处理后的请求参数%1$s为：%2$s", key, encodeValue);
//                    formBodyBuilder.add(key, encodeValue);
//                }
//                newRequestBuilder.method(method, formBodyBuilder.build());
//                Timber.d("执行FormBody请求逻辑处理结束");
//                return chain.proceed(newRequestBuilder.build());
//
//            } else if(originalRequest.body() instanceof MultipartBody) {
//                Timber.d("执行MultipartBody请求逻辑开始");
////                MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
//
////                List<MultipartBody.Part> parts = ((MultipartBody) originalRequest.body()).parts();
////                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), value);
////                MultipartBody.Part part = MultipartBody.Part.createFormData(key, value);
////
////                multipartBodyBuilder.addPart(part);
//
////                newRequestBuilder.method(originalRequest.method(), multipartBodyBuilder.build());
//                Timber.d("执行MultipartBody请求逻辑处理结束");
//            } else {
//                Timber.d("执行其他请求逻辑");
//                //  处理其它类型的request.body
//            }
//        }

        return chain.proceed(originalRequest);
    }

//    /**
//     * des3加密
//     *
//     * @param value 原始字符串
//     * @return 加密后的字符串
//     */
//    private String des3Encode(String value) {
//        try {
//            value = URLEncoder.encode(value, "utf-8");
//            value = ChiperNative.Des3Encode(value);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        return value;
//    }
}
