package google.architecture.coremodel.http.service.core.subscriber;

import android.text.TextUtils;

import com.apkfuns.logutils.LogUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import google.architecture.coremodel.http.result.base.Response;
import google.architecture.coremodel.http.service.config.APIConstant;
import google.architecture.coremodel.http.service.core.CallBack;
import io.reactivex.observers.DisposableObserver;

/**
 * 预处理请求结果类
 *
 * @author Jewel
 * @version 1.0
 * @since 2017/6/14 0014
 */

public class BaseSubscriber<T> extends DisposableObserver<Response<T>> {

    private CallBack<Response<T>> callBack;

    public BaseSubscriber(CallBack<Response<T>> callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onNext(Response<T> response) {
        //业务代码为成功则将具体的数据返回
        if (TextUtils.equals(response.code, APIConstant.NET_CODE_SUCCESS)) {
            if (callBack != null) callBack.handlerSuccess(response);
        } else {
            if (callBack != null) {
                callBack.fail(response.bizCode, response.message);
                callBack.handleBreak(response.bizCode, response.message, null);
            }
        }
        LogUtils.d("BaseSubscriber onNext(Response)");
    }

    @Override
    public void onError(Throwable t) {
        LogUtils.d("BaseSubscriber onError(Throwable)");
        String code;
        String msg;
        // 处理常见的连接错误
        if (t instanceof SocketTimeoutException) {
            code = APIConstant.NET_CODE_SOCKET_TIMEOUT;
            msg = APIConstant.SOCKET_TIMEOUT_EXCEPTION;
        } else if (t instanceof ConnectException) {
            code = APIConstant.NET_CODE_CONNECT;
            msg = APIConstant.CONNECT_EXCEPTION;
        } else if (t instanceof UnknownHostException) {
            code = APIConstant.NET_CODE_UNKNOWN_HOST;
            msg = APIConstant.UNKNOWN_HOST_EXCEPTION;
        } else {
            code = APIConstant.NET_CODE_ERROR;
            msg = t.getMessage();
        }
        LogUtils.e("请求发生错误：%s", t);
        if (callBack != null) {
            callBack.handleBreak(code, msg, t);
            callBack.error(t);
            callBack.finish();
        }
    }

    @Override
    public void onComplete() {
        LogUtils.d("BaseSubscriber onComplete()");
        if (callBack != null) {
            callBack.finish();
        }
    }
}
