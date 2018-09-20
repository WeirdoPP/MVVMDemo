package google.architecture.coremodel.http.service.core.interceptor;

import android.content.Context;
import android.text.TextUtils;

import com.apkfuns.logutils.LogUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import google.architecture.coremodel.util.NetworkUtil;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author zhanglianglin
 * @version 1.0
 * @desc 缓存拦截器
 * @since 2018/09/18
 */

public class CacheInterceptor implements Interceptor {

    /**
     * 后期请求头信息
     */
    public static final String HEADER_GET_DATA_TYPE = "getDataType";
    public static final String HEADER_GET_DATA_FROM_NET = "getDataType: 0";
    public static final String HEADER_DYNAMIC_CONDITION_GET = "getDataType: 1";
    public static final String HEADER_GET_DATA_FROM_CACHE = "getDataType: 2";
    /**
     * 强制从网络获取
     */
    public static final int CONSTRAINT_GET_FROM_NET = 0;
    /**
     * 有网时从请求头获取信息获取(动态)
     */
    public static final int DYNAMIC_CONDITION_GET = 1;
    /**
     * 无网时必须请求缓存(强制缓存)
     */
    public static final int CONSTRAINT_GET_FROM_CACHE = 2;
    //这是设置在多长时间范围内获取缓存里面
    public CacheControl FORCE_CACHE1 = new CacheControl.Builder()
            .onlyIfCached()
            .maxStale(1, TimeUnit.HOURS)//CacheControl.FORCE_CACHE--是int型最大值
            .build();
    private Context context;

    public CacheInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        if (NetworkUtil.isConnected(context)) {//有网时
            Response response = chain.proceed(request);
            String getDataType = request.header(HEADER_GET_DATA_TYPE);
            int netGet = CONSTRAINT_GET_FROM_NET;
            if (!TextUtils.isEmpty(getDataType)) {
                netGet = Integer.valueOf(getDataType);
            }
            switch (netGet) {
                case CONSTRAINT_GET_FROM_NET://强制网络
                    LogUtils.d("强制或默认网络获取数据");
                    return response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + 0)
                            .build();
                case DYNAMIC_CONDITION_GET://根据请求头信息设置时间来判断
                    String cacheControl = request.cacheControl().toString();
                    LogUtils.d("根据请求头信息设置时间来判断" + cacheControl);
                    return response.newBuilder()
                            .header("Cache-Control", cacheControl)
                            .build();
                case CONSTRAINT_GET_FROM_CACHE://强制缓存
                    LogUtils.d("强制缓存");
                    request = request.newBuilder()
                            .cacheControl(FORCE_CACHE1)//此处不设置过期时间
                            .build();
                    Response cacheResponse = chain.proceed(request);
                    return cacheResponse.newBuilder()
                            .header("Cache-Control", "public, only-if-cached")
                            .build();
            }
            return null;
        } else {//无网时
            LogUtils.d("没有网络,强制缓存获取");
            request = request.newBuilder()
                    .cacheControl(FORCE_CACHE1)
                    .build();
        }

        Response response = chain.proceed(request);
        //下面注释的部分设置也没有效果，因为在上面已经设置了
        return response.newBuilder()
                .header("Cache-Control", "public, only-if-cached")
                .build();
    }
}
