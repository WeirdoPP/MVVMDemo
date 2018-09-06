package google.architecture.coremodel.http.service.core;

import android.support.annotation.CallSuper;

import google.architecture.coremodel.http.result.base.Response;


/**
 * @author Jewel
 * @version 1.0
 * @since 2017/8/14 0014
 */

public abstract class CallbackSuccess<T extends Response> implements CallBack<T> {


    @Override
    public void fail(String code, String message) {

    }

    @Override
    public void error(Throwable throwable) {

    }

    @CallSuper
    @Override
    public void handleBreak(String code, String message, Throwable throwable) {
//        BaseApplication.showLongToast(message);
//        if (TextUtils.equals(APIConstant.NET_CODE_NEED_LOGIN, code)) { // 重新登录
//            CommonIntent.gotoLogin();
//        }
    }

    @Override
    public void finish() {

    }
}
