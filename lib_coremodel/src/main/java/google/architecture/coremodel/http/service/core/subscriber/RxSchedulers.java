package google.architecture.coremodel.http.service.core.subscriber;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import java.net.ConnectException;
import java.util.concurrent.TimeUnit;

import google.architecture.coremodel.http.LoadingDialog;
import google.architecture.coremodel.http.service.config.APIConstant;
import google.architecture.coremodel.http.service.core.CallBack;
import google.architecture.coremodel.util.NetworkUtil;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 对请求进行预处理和简化每次都要写的线程步骤
 *
 * @author Jewel
 * @version 1.0
 * @since 2017/6/14 0014
 * <p>
 */

public class RxSchedulers {

    /**
     * 基本请求
     */
    public static <T> FlowableTransformer<T, T> mainThread(final Context context, final CallBack callBack) {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(@NonNull Flowable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Subscription>() {
                            @Override
                            public void accept(@NonNull Subscription subscription) throws Exception {
                                // 如果无网络，则直接取消
                                if (!NetworkUtil.isConnected(context)) {
                                    if (null != callBack) {
                                        callBack.handleBreak(APIConstant.NET_CODE_NOT_CONNECT,
                                                APIConstant.CONNECT_EXCEPTION, new ConnectException("网络出错,请重试"));
                                        callBack.fail(APIConstant.NET_CODE_NOT_CONNECT, APIConstant.CONNECT_EXCEPTION);
                                        callBack.finish();
                                    }
                                    cancel(subscription);
                                }
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 带进度条的请求
     */
    public static <T> FlowableTransformer<T, T> mainThread(final Context context, final LoadingDialog dialog, final CallBack callBack) {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(@NonNull Flowable<T> upstream) {
                return upstream
                        .delay(1, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Subscription>() {
                            @Override
                            public void accept(@NonNull final Subscription subscription) throws Exception {
                                if (!NetworkUtil.isConnected(context)) {
                                    if (null != callBack) {
                                        callBack.handleBreak(APIConstant.NET_CODE_NOT_CONNECT,
                                                APIConstant.CONNECT_EXCEPTION, new ConnectException("网络出错,请重试"));
                                        callBack.fail(APIConstant.NET_CODE_NOT_CONNECT, APIConstant.CONNECT_EXCEPTION);
                                        callBack.finish();
                                    }
                                    cancel(subscription);
                                } else {
                                    // 启动进度显示，取消进度时会取消请求
                                    if (dialog != null) {
                                        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                            @Override
                                            public void onCancel(DialogInterface dialog) {
                                                subscription.cancel();
                                            }
                                        });
                                        dialog.show();
                                    }
                                }
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 带进度条的请求
     */
    public static <T> FlowableTransformer<T, T> mainThread(final Context context, final LoadingDialog dialog,
                                                           final OnSchedulerCancelListener listener, final CallBack callBack) {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(@NonNull Flowable<T> upstream) {
                return upstream
                        .delay(1, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Subscription>() {
                            @Override
                            public void accept(@NonNull final Subscription subscription) throws Exception {
                                if (!NetworkUtil.isConnected(context)) {
                                    if (null != callBack) {
                                        callBack.handleBreak(APIConstant.NET_CODE_NOT_CONNECT,
                                                APIConstant.CONNECT_EXCEPTION, new ConnectException("网络出错,请重试"));
                                        callBack.fail(APIConstant.NET_CODE_NOT_CONNECT, APIConstant.CONNECT_EXCEPTION);
                                        callBack.finish();
                                    }
                                    cancel(subscription);
                                    if (dialog != null) {
                                        dialog.dismiss();
                                    }
                                } else {
                                    // 启动进度显示，取消进度时会取消请求
                                    if (dialog != null) {
                                        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                            @Override
                                            public void onCancel(DialogInterface dialog) {
                                                subscription.cancel();
                                                if (listener != null) {
                                                    listener.onSchedulerCancel();
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    private static void cancel(@NonNull Subscription subscription) {
        subscription.cancel();
    }

    /**
     * 调度器取消监听
     */
    public interface OnSchedulerCancelListener {
        /**
         * 调度器已取消事件
         */
        void onSchedulerCancel();
    }
}
