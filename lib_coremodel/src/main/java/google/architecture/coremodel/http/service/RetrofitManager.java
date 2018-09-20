package google.architecture.coremodel.http.service;


import android.content.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static google.architecture.coremodel.http.service.config.APIConstant.GANK_HOST;

/**
 * Retrofit管理类
 *
 * @author Jewel
 * @version 1.0
 * @since 2017/6/14 0014
 */
// FIXME: 2018/9/3 修复debug模式下添加log信息拦截
public class RetrofitManager {

    public static final int CONNECT_TIMEOUT = 30;
    public static final int WRITE_TIMEOUT = 60;
    public static final int READ_TIMEOUT = 60;
    private static final long CACHE_SIZE = 1024 * 1024 * 50;

    private static Map<String, CompositeDisposable> netManager = new HashMap<>();

    public static Retrofit getInstance(Context context) {
        return Instance.getRetrofit(context);
    }

    /**
     * 为了避免错误的取消了，key建议使用packageName + className
     */
    public static void add(String key, Disposable disposable) {
        if (netManager.containsKey(key)) {
            netManager.get(key).add(disposable);
        } else {
            CompositeDisposable compositeDisposable = new CompositeDisposable();
            compositeDisposable.add(disposable);
            netManager.put(key, compositeDisposable);
        }
    }

    public static void remove(String key) {
        if (netManager.containsKey(key)) {
            CompositeDisposable compositeDisposable = netManager.get(key);
            compositeDisposable.clear();
        }
    }

    private static class Instance {
        private static String baseUrl = GANK_HOST;

        private static Retrofit getRetrofit(Context context) {
            OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
            // debug模式添加log信息拦截
//            if (BaseApplication.getInstance().isDebug()) {
//                httpBuilder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
//            }

//            httpBuilder.addInterceptor(new HeaderInterceptor()); // 请求头统一封装拦截
//            httpBuilder.addInterceptor(new ParamsInterceptor()); // 请求参数统一加密处理
//            httpBuilder.addInterceptor(new ResponseInterceptor()); // 响应结果统一处理

            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();//打印返回级别
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpBuilder.addInterceptor(httpLoggingInterceptor);

            //设置网络缓存目录以及添加缓存拦截器
//            File cacheFile = new File(context.getCacheDir(), "mvvm_demo");
//            Cache cache = new Cache(cacheFile, CACHE_SIZE);
//            httpBuilder.cache(cache);
//            httpBuilder.addNetworkInterceptor(new CacheInterceptor(context));

            // 设置重试
            httpBuilder.retryOnConnectionFailure(true);
            // 设置连接超时
            httpBuilder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
            //设置写超时
            httpBuilder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
            //设置读超时
            httpBuilder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);

            Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
            retrofitBuilder.baseUrl(baseUrl);
            retrofitBuilder.client(httpBuilder.build());
            retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
            retrofitBuilder.addConverterFactory(GsonConverterFactory.create());
            return retrofitBuilder.build();
        }
    }
}
