package google.architecture.coremodel.http;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import google.architecture.coremodel.R;


/**
 * 对话框基类
 *
 * @author Jewel
 * @version 1.2
 * @since 2016/9/5 0005
 */

public class BaseDialog extends Dialog {

    public static final int MATCH_PARENT = WindowManager.LayoutParams.MATCH_PARENT;
    public static final int WRAP_CONTENT = WindowManager.LayoutParams.WRAP_CONTENT;
    public static final float DEFAULT_DIM = 0.4f;
    protected View rootView;

    public BaseDialog(Context context) {
        this(context, R.style.loading_dialog, false);
    }

    public BaseDialog(Context context, boolean cancelable) {
        this(context, R.style.loading_dialog, cancelable);
    }

    public BaseDialog(Context context, int themeResId, boolean cancelable) {
        this(context, themeResId, cancelable, false);
    }

    public BaseDialog(Context context, boolean cancelable, boolean canceledOnTouchOutside) {
        this(context, R.style.loading_dialog, cancelable, canceledOnTouchOutside);
    }

    public BaseDialog(Context context, int themeResId, boolean cancelable, boolean canceledOnTouchOutside) {
        super(context, themeResId);
        setCancelable(cancelable);
        setCanceledOnTouchOutside(canceledOnTouchOutside);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Window window = getWindow();
        if (window == null) {
            return;
        }
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.dimAmount = getDimAmount();
        layoutParams.width = getWidth();
        layoutParams.height = getHeight();
        layoutParams.gravity = getGravity();

        window.setAttributes(layoutParams);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(getContext()).inflate(getContentView(), null, false);
        setContentView(rootView);
        afterSetContentView();
        initView();
    }


    protected void afterSetContentView() {
        // 拦截Home键
        getWindow().addFlags(3);
//		if (BuildConfig.INTERCEPT_FUNCTION_KEY) {
//			// 拦截多任务键
//			getWindow().addFlags(5);
//		}
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_HOME:
                return true;
            case KeyEvent.KEYCODE_BACK:  // 拦截Back键
                onBackPressed();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 绑定Dialog的自定义layout资源
     *
     * @return layout资源ID
     */
    @LayoutRes
    protected int getContentView() {
        return -1;
    }

    /**
     * 初始化操作
     */
    protected void initView() {
    }

    /**
     * 背景暗淡系数，通过重载该返回设置该系数
     *
     * @return {@link  BaseDialog#DEFAULT_DIM}
     */
    protected float getDimAmount() {
        return DEFAULT_DIM;
    }

    /**
     * 对话框宽度，通过重载该方法设置该系数，默认{@link BaseDialog#WRAP_CONTENT}
     *
     * @return {@link BaseDialog#WRAP_CONTENT}, {@link BaseDialog#MATCH_PARENT}, 其他自定义的宽度
     */
    protected int getWidth() {
        return WRAP_CONTENT;
    }

    /**
     * 对话框高度，通过重载该方法设置该系数，默认{@link BaseDialog#WRAP_CONTENT}
     *
     * @return {@link BaseDialog#WRAP_CONTENT}, {@link BaseDialog#MATCH_PARENT}, 其他自定义的高度
     */
    protected int getHeight() {
        return WRAP_CONTENT;
    }

    /**
     * 对话框显示位置，通过重载该方法设置该系数，默认{@link Gravity#CENTER}
     *
     * @return 参考 {@link Gravity}
     */
    protected int getGravity() {
        return Gravity.CENTER;
    }


}
