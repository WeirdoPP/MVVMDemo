package google.architecture.coremodel.http.service.providers;

import java.util.concurrent.TimeUnit;

import google.architecture.coremodel.datamodel.http.entities.GirlsData;
import google.architecture.coremodel.http.result.base.Response;
import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.LifeCache;

/**
 * @author zhanglianglin
 * @version 1.0
 * @desc 登录
 * @since 2018/09/20
 */

public interface LoginProviders {
    /**
     * LifeCache设置缓存过期时间. 如果没有设置@LifeCache ,
     * 数据将被永久缓存理除非你使用了 EvictProvider,EvictDynamicKey or EvictDynamicKeyGroup .
     *
     * @param user
     * @param userName        驱逐与一个特定的键使用EvictDynamicKey相关的数据。比如分页，排序或筛选要求
     * @param evictDynamicKey 可以明确地清理指定的数据 DynamicKey.
     * @return
     */
    @LifeCache(duration = 90, timeUnit = TimeUnit.SECONDS)
    Observable<Response<GirlsData>> getGirlsDatas(Observable<Response<GirlsData>> user, DynamicKey userName, EvictDynamicKey evictDynamicKey);
}
