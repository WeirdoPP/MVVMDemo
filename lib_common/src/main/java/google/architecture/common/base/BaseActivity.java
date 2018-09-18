package google.architecture.common.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.apkfuns.logutils.LogUtils;

import google.architecture.common.R;
import google.architecture.common.util.Utils;

/**
 * adb 查看 Activity 的命令 : adb shell dumpsys activity
 * <p>
 *  standard:
 *  标准模式，也是系统的默认模式，每次启动一个 Activity 都会重新创建一个新的实例，不管这个实例是否存在，被创建的实例的生命周期符合我们之前所说的正常情况下的生命周期的过程
 *  <p>
 *  singleTop:
 *  栈顶复用模式，在这种模式下，如果新 Activity 已经位于任务栈的栈顶，那么此 Activity 不会被重新创建，
 *   同时它的 onNewIntent 方法会被调用，通过此方法的参数，我们可以取出当前的请求信息，需要注意的是这个
 *   的 onCreate、onStart 不会被系统调用，因为它并没有发生改变，如果新的 Activity 已经存在，但是并没有处在栈顶，
 *   那么这个 Activity 任然会被创建，这里举个例子，假设目前栈内的情况是 ABCD,其中 ABCD 为 4 个 Activity，
 *   A位于栈低，D 位于栈顶，这个时候要再次启动 D，如果 D 的启动模式为 singleTop, 那么栈内的情况仍然为 ABCD，
 *   如果 D 的启动模式为 standard，那么由于 D 被重新创建，导致栈内的情况就变为 ABCDD
 *  <p>
 *  singleTask:
 *  栈内复用模式，这是一种单实例模式，在这种模式下，只要 Activity 在一个栈中存在，那么多次启动此 Activity 都不会再重新创建实例，
 *   和 singleTop 一样，系统也会回调它的 onNewIntent 方法，具体一点，当一个 Activity 的启动模式是 singleTask 模式时，
 *   比如 Activity A，系统会首先去找是否存在 A 想要的任务栈，如果不存在，就重新创建一个任务栈，然后创建 A 的实例并放到栈中，如果存在 A 所需要的任务栈，
 *   这时要看 A 实例在栈中是否存在，如果不存在，就创建 A 实例，如果存在，就把 A 调到栈顶，并调用 onNewIntent 方法，下面举个例子：
 *  1）比如目前任务栈 S1 的情况为 ABC，这个时候 D 以 singleTask 的方式请求启动，它所需要的任务栈为 S2，由于 S2 和 D 都不存在，所以会先创建S2，然后创建 D 的实例并放入栈中
 *  2）另一种情况，假如 D 所需要的任务栈为 S1，其他情况如上，那么由于 S1 已经存在，系统就会直接创建 D 的实例并将其入栈到 S1
 *  3）如果 D 所需要的任务栈为 S1，并且当前任务栈的情况为 ADBC，根据栈内复用的原则，系统会把 D 切换到栈顶并调用其 onNewIntent 方法，
 *   同时singleTask 模式具有 clearTop 的效果，会导致栈内所有在 D 上面的 Activity 全部出栈，即全部销毁，于是最后的情况为 AD
 *  <p>
 *  singleInstance:
 *  单实例模式，这是一种加强的 singleTask 模式，它除了具有 singleTask 的全部属性外，那就是这种模式的 Activity 只能单独的位于一个任务栈中
 */
public class BaseActivity<VM extends BaseViewModel> extends AppCompatActivity {

    protected VM viewModel;

    /**
     * 封装的findViewByID方法
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T $(@IdRes int id) {
        return (T) super.findViewById(id);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewManager.getInstance().addActivity(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewManager.getInstance().finishActivity(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * 获取ViewModel
     *
     * @return
     */
    public VM getViewModel() {
        if (viewModel == null) {
            LogUtils.e("请调用setViewModel()");
        }
        return viewModel;
    }

    /**
     * 设置ViewModel
     *
     * @param viewModel
     */
    public void setViewModel(VM viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Setup the toolbar.
     *
     * @param toolbar   toolbar
     * @param hideTitle 是否隐藏Title
     */
    protected void setupToolBar(Toolbar toolbar, boolean hideTitle) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            if (hideTitle) {
                //隐藏Title
                actionBar.setDisplayShowTitleEnabled(false);
            }
        }
    }


    /**
     * 添加fragment
     *
     * @param fragment
     * @param frameId
     */
    protected void addFragment(BaseFragment fragment, @IdRes int frameId) {
        Utils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .add(frameId, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commitAllowingStateLoss();

    }


    /**
     * 替换fragment
     *
     * @param fragment
     * @param frameId
     */
    protected void replaceFragment(BaseFragment fragment, @IdRes int frameId) {
        Utils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .replace(frameId, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commitAllowingStateLoss();

    }


    /**
     * 隐藏fragment
     *
     * @param fragment
     */
    protected void hideFragment(BaseFragment fragment) {
        Utils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .hide(fragment)
                .commitAllowingStateLoss();

    }


    /**
     * 显示fragment
     *
     * @param fragment
     */
    protected void showFragment(BaseFragment fragment) {
        Utils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .show(fragment)
                .commitAllowingStateLoss();

    }


    /**
     * 移除fragment
     *
     * @param fragment
     */
    protected void removeFragment(BaseFragment fragment) {
        Utils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .remove(fragment)
                .commitAllowingStateLoss();

    }


    /**
     * 弹出栈顶部的Fragment
     */
    protected void popFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.activity_down_in, R.anim.activity_down_out);
    }
}
