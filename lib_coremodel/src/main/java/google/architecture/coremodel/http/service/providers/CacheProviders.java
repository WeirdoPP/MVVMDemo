package google.architecture.coremodel.http.service.providers;


import com.mvvm.opensoure.AppRootFileUtils;

import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;

/**
 * @author zhanglianglin
 * @version 1.0
 * @desc 缓存
 * @since 2018/09/19
 */

public class CacheProviders {

    public static final String CACHE_PATH = AppRootFileUtils.get().getImgCachePath() + "/data_cache.txt";

    private static LoginProviders loginProviders;

    public synchronized static LoginProviders getLoginProviders() {
        if (loginProviders == null) {
            loginProviders = new RxCache.Builder()
                    .persistence(AppRootFileUtils.createFile(CACHE_PATH), new GsonSpeaker())//缓存文件的配置、数据的解析配置
                    .using(LoginProviders.class);//这些配置对应的缓存接口
        }
        return loginProviders;
    }
}
