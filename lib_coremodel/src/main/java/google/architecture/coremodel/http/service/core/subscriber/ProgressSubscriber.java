package google.architecture.coremodel.http.service.core.subscriber;


import com.apkfuns.logutils.LogUtils;

import google.architecture.coremodel.http.LoadingDialog;
import google.architecture.coremodel.http.result.base.Response;
import google.architecture.coremodel.http.service.core.CallBack;

/**
 * ╭╯☆★☆★╭╯
 * 　　╰╮★☆★╭╯
 * 　　　 ╯☆╭─╯
 * 　　 ╭ ╭╯
 * 　╔╝★╚╗  ★☆╮     BUG兄，没时间了快上车    ╭☆★
 * 　║★☆★║╔═══╗　╔═══╗　╔═══╗  ╔═══╗
 * 　║☆★☆║║★　☆║　║★　☆║　║★　☆║  ║★　☆║
 * ◢◎══◎╚╝◎═◎╝═╚◎═◎╝═╚◎═◎╝═╚◎═◎╝..........
 *
 * @author Jewel
 * @version 1.0
 * @since 2017/6/14 0014
 */

public class ProgressSubscriber<T> extends BaseSubscriber<T> {

    private LoadingDialog dialog;

    public ProgressSubscriber(LoadingDialog dialog, CallBack<Response<T>> callBack) {
        super(callBack);
        this.dialog = dialog;
    }

    @Override
    public void onError(Throwable t) {
        super.onError(t);
        LogUtils.d("调用ProgressSubscriber onError(Throwable)");
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }

    @Override
    public void onComplete() {
        super.onComplete();
        LogUtils.d("调用ProgressSubscriber onComplete()");
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }
}
