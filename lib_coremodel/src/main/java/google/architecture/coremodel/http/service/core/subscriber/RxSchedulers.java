package google.architecture.coremodel.http.service.core.subscriber;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import google.architecture.coremodel.http.LoadingDialog;
import google.architecture.coremodel.http.service.core.CallBack;
import google.architecture.coremodel.util.NetworkUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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
    public static <T> ObservableTransformer<T, T> mainThread(final Context context, final CallBack callBack) {

        return new ObservableTransformer<T, T>() {

            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
//                                if (!NetworkUtil.isConnected(context)) {
//                                    if (null != callBack) {
//                                        callBack.handleBreak(APIConstant.NET_CODE_NOT_CONNECT,
//                                                APIConstant.CONNECT_EXCEPTION, new ConnectException("网络出错,请重试"));
//                                        callBack.fail(APIConstant.NET_CODE_NOT_CONNECT, APIConstant.CONNECT_EXCEPTION);
//                                        callBack.finish();
//                                    }
//                                    cancel(disposable);
//                                }
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 带进度条的请求
     */
    public static <T> ObservableTransformer<T, T> mainThread(final Context context, final LoadingDialog dialog, final CallBack callBack) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream
                        .delay(1, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(@NonNull final Disposable disposable) throws Exception {
                                if (!NetworkUtil.isConnected(context)) {//需要判断是否需要读缓存才能解封注释代码
//                                    if (null != callBack) {
//                                        callBack.handleBreak(APIConstant.NET_CODE_NOT_CONNECT,
//                                                APIConstant.CONNECT_EXCEPTION, new ConnectException("网络出错,请重试"));
//                                        callBack.fail(APIConstant.NET_CODE_NOT_CONNECT, APIConstant.CONNECT_EXCEPTION);
//                                        callBack.finish();
//                                    }
//                                    cancel(disposable);
                                } else {
                                    // 启动进度显示，取消进度时会取消请求
                                    if (dialog != null) {
                                        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                            @Override
                                            public void onCancel(DialogInterface dialog) {
                                                disposable.dispose();
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
    public static <T> ObservableTransformer<T, T> mainThread(final Context context, final LoadingDialog dialog,
                                                             final OnSchedulerCancelListener listener, final CallBack callBack) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream
                        .delay(1, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(@NonNull final Disposable disposable) throws Exception {
                                if (!NetworkUtil.isConnected(context)) {
//                                    if (null != callBack) {
//                                        callBack.handleBreak(APIConstant.NET_CODE_NOT_CONNECT,
//                                                APIConstant.CONNECT_EXCEPTION, new ConnectException("网络出错,请重试"));
//                                        callBack.fail(APIConstant.NET_CODE_NOT_CONNECT, APIConstant.CONNECT_EXCEPTION);
//                                        callBack.finish();
//                                    }
//                                    cancel(disposable);
//                                    if (dialog != null) {
//                                        dialog.dismiss();
//                                    }
                                } else {
                                    // 启动进度显示，取消进度时会取消请求
                                    if (dialog != null) {
                                        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                            @Override
                                            public void onCancel(DialogInterface dialog) {
                                                disposable.dispose();
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

    private static void cancel(@NonNull Disposable disposable) {
        disposable.dispose();
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
