package google.architecture.coremodel.http.service;

import android.content.Context;

import google.architecture.coremodel.datamodel.http.entities.GirlsData;
import google.architecture.coremodel.http.LoadingDialog;
import google.architecture.coremodel.http.result.base.Response;
import google.architecture.coremodel.http.service.api.CommonApi;
import google.architecture.coremodel.http.service.core.CallBack;
import google.architecture.coremodel.http.service.providers.CacheProviders;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;

/**
 * @author zhanglianglin
 * @version 1.0
 * @desc 公共请求
 * @since 2018/09/03
 */

public class CommonHttp extends BaseHttp {

    public static CommonApi getCommonApi(Context context) {
        return RetrofitManager.getInstance(context).create(CommonApi.class);
    }

    /**
     * 获取女孩图片集合
     *
     * @param context
     * @param size
     * @param index
     * @param callBack
     * @return
     */
    public static Disposable getGirlsDatas(Context context, LoadingDialog dialog, String size, String index, CallBack<Response<GirlsData>> callBack) {
        Observable observable = CacheProviders.getLoginProviders()
                .getGirlsDatas(CommonHttp.getCommonApi(context).getGirlsDatas(size, index)
                        , new DynamicKey("getGirlsDatas"), new EvictDynamicKey(false));
        return subscribeWithLoading(context, dialog, observable, callBack);
    }


}
