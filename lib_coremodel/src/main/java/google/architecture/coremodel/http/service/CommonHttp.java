package google.architecture.coremodel.http.service;

import android.content.Context;

import google.architecture.coremodel.datamodel.http.entities.GirlsData;
import google.architecture.coremodel.http.LoadingDialog;
import google.architecture.coremodel.http.result.base.Response;
import google.architecture.coremodel.http.service.api.CommonApi;
import google.architecture.coremodel.http.service.core.CallBack;
import io.reactivex.disposables.Disposable;

/**
 * @author zhanglianglin
 * @version 1.0
 * @desc 公共请求
 * @since 2018/09/03
 */

public class CommonHttp extends BaseHttp {

    public static CommonApi getOrderMealApi() {
        return RetrofitManager.getInstance().create(CommonApi.class);
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
        return subscribeWithLoading(context, dialog, getOrderMealApi().getGirlsDatas(size, index), callBack);
    }
}
