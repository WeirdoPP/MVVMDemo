package google.architecture.coremodel.http.service.core.interceptor;


import com.apkfuns.logutils.LogUtils;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * @author Jewel
 * @version 1.0
 * @since 2017/7/20 0020
 * <p>
 * 定义服务器响应拦截器
 */
// FIXME: 2018/9/3 自定义返回信息拦截
public class ResponseInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
//        String status = originalResponse.header(APIKey.HEAD_RESPONSE_STATUS);
//        Timber.d("报文head：%s \n", originalResponse.headers().toString());
        final String result = readResponseStr(originalResponse);
        LogUtils.d("服务器返回：", result);
//
//        String token = originalResponse.header(APIKey.LZ_TOKEN);
//        String url = originalResponse.request().url().toString();
//        Timber.d("获取到的url========#%s", url);
//        String loginUrl = BaseApplication.getInstance().getBaseUrl() + APIUrl.LOGIN;
//        if (TextUtils.equals(loginUrl, url) && !TextUtils.isEmpty(token)) {  // 只保存登录接口返回的token
//            SharedPreUtil.saveToken(token);
//            Timber.d("获取到的token========#%s", token);
//        }
//
//
//        if (!TextUtils.isEmpty(result)) {
//            if(result.startsWith("{") || result.startsWith("[")) {
//                return originalResponse;
//            } else {
//                ResponseBody body = originalResponse.body();
//                com.zhiyuan.httpservice.model.response.Response response = new com.zhiyuan.httpservice.model.response.Response();
//                response.code = APIConstant.NET_CODE_SERVICE_ERROR;
//                response.message = result;
//
//                return originalResponse.newBuilder()
//                        .body(ResponseBody.create(body == null ? null : body.contentType(), GsonUtil.gson().toJson(response)))
//                        .build();
//            }
//        } else {
//            ResponseBody body = originalResponse.body();
//            com.zhiyuan.httpservice.model.response.Response response = new com.zhiyuan.httpservice.model.response.Response();
//            response.code = APIConstant.NET_CODE_SERVICE_ERROR;
//            response.message = APIConstant.SERVICE_EXCEPTION;
//
//            return originalResponse.newBuilder()
//                    .body(ResponseBody.create(body == null ? null : body.contentType(), GsonUtil.gson().toJson(response)))
//                    .build();
//        }
        return null;
    }

    /**
     * 读取Response返回String内容
     *
     * @param response 原响应实体
     * @return 实际消息
     */
    private String readResponseStr(Response response) {
        ResponseBody body = response.body();
        BufferedSource source = body.source();
        try {
            source.request(Long.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        MediaType contentType = body.contentType();
        Charset charset = Charset.forName("UTF-8");
        if (contentType != null) {
            charset = contentType.charset(charset);
        }
        String s = null;
        Buffer buffer = source.buffer();
        if (isPlaintext(buffer)) {
            s = buffer.clone().readString(charset);
        }
        return s;
    }

    /**
     * 是否明文
     *
     * @param buffer 内容缓存
     * @return 明文为 {@link true}
     */
    private boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

//    /**
//     * 根据请求头处理返回结果：<p>
//     * 如果statues为{@link APIConstant.HEADER_RESPONSE_STATUS#ENCRYPT_RSA}，进行RSA解密；<p>
//     * 如果statues为{@link APIConstant.HEADER_RESPONSE_STATUS#ENCRYPT_SYMMETRIC}，则进行3des解密。
//     *
//     * @param status   请求头{@link APIKey#HEAD_RESPONSE_STATUS}字段值
//     * @param response 接口返回原始数据
//     * @return 处理后的数据
//     */
//    private String handleResponse(String status, String response) {
//        String decodeResponse = response;
//        try {
//            if (TextUtils.equals(status, APIConstant.HEADER_RESPONSE_STATUS.ENCRYPT_RSA.getValue())) {
//                byte[] des = RSAUtils.decryptByPrivateKey(Base64Utils.decode(response.trim()), RSAUtils.getPrivateKey());
//                decodeResponse = new String(des);
//            } else if (TextUtils.equals(status, APIConstant.HEADER_RESPONSE_STATUS.ENCRYPT_SYMMETRIC.getValue())) {
//                decodeResponse = ChiperNative.Des3Decode(response);
//                decodeResponse = URLDecoder.decode(decodeResponse, "utf-8");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return decodeResponse;
//    }

}
