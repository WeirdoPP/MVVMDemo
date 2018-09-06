package google.architecture.coremodel.http.service;

import android.content.Context;
import android.support.annotation.NonNull;

import google.architecture.coremodel.http.LoadingDialog;
import google.architecture.coremodel.http.result.base.Response;
import google.architecture.coremodel.http.service.core.CallBack;
import google.architecture.coremodel.http.service.core.subscriber.BaseSubscriber;
import google.architecture.coremodel.http.service.core.subscriber.ProgressSubscriber;
import google.architecture.coremodel.http.service.core.subscriber.RxSchedulers;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;

/**
 * @author Jewel
 * @version 1.0
 * @since 2017/7/19 0019
 */

public class BaseHttp {

    /**
     * 无加载中对话框的订阅方法
     */
    protected static <T> Disposable subscribe(@NonNull Context context, Flowable<Response<T>> flowable, CallBack<Response<T>> callBack) {
        return flowable.compose(RxSchedulers.<Response<T>>mainThread(context, callBack))
                .subscribeWith(new BaseSubscriber<>(callBack));
    }

    /**
     * 带加载中对话框的订阅方法
     */
    protected static <T> Disposable subscribeWithLoading(@NonNull Context context, LoadingDialog dialog, Flowable<Response<T>> flowable,
                                                         CallBack<Response<T>> callBack) {
        return flowable.compose(RxSchedulers.<Response<T>>mainThread(context, dialog, callBack))
                .subscribeWith(new ProgressSubscriber<>(dialog, callBack));
    }

}
