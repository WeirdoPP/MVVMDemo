package google.architecture.coremodel.http;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.apkfuns.logutils.LogUtils;

import google.architecture.coremodel.R;


/**
 * 通用加载对话框
 *
 * @author Jewel
 * @version 1.0
 * @since 2017/8/23 0023
 */
//@Route(path = "/http/LoadingDialog")
public class LoadingDialog extends BaseDialog {

    private long clickBackDownSpace;
    private int clickBackDownCount;

    public LoadingDialog(Context context, boolean cancelable) {
        super(context, R.style.loading_dialog, cancelable, false);
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_loading;
    }

    @Override
    protected float getDimAmount() {
        return 0f;
    }

    @Override
    public void show() {
        if (isShowing()) {
            return;
        }

        if (((ContextWrapper) getContext()).getBaseContext() instanceof Application) {
            Window window = getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                window.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
            } else {
                window.setType(WindowManager.LayoutParams.TYPE_TOAST);
            }
            LogUtils.d("LoadingDialog context= %s", "context 为 Application！");
        } else {
            LogUtils.d("LoadingDialog context= %s", "context 为 Activity！");
        }
        super.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        // 拦截音量减键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - clickBackDownSpace > 2000) {
                clickBackDownCount = 0;
                clickBackDownSpace = System.currentTimeMillis();
            }
            if (clickBackDownCount < 5)
                clickBackDownCount++;
            else {
                dismiss();
                clickBackDownCount = 0;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
