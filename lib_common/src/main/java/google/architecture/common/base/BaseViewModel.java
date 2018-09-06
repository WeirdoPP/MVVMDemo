package google.architecture.common.base;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.apkfuns.logutils.LogUtils;

import google.architecture.coremodel.http.LoadingDialog;
import google.architecture.coremodel.http.service.RetrofitManager;
import io.reactivex.disposables.Disposable;

/**
 * @author zhanglianglin
 * @version 1.0
 * @desc 接口请求的时候, 比如有一个是
 * @since 2018/09/03
 */
public class BaseViewModel extends AndroidViewModel {

    protected static final MutableLiveData ABSENT = new MutableLiveData();

    {
        //noinspection unchecked
        ABSENT.setValue(null);
    }


    public BaseViewModel(@NonNull Application application) {
        super(application);
        LogUtils.d("=======BaseViewModel--onCreate=========");
    }

    /**
     * 添加监听
     *
     * @param disposable
     */
    public void addHttpListener(Disposable disposable) {
        RetrofitManager.add(this.getClass().getSimpleName(), disposable);
    }

//    /**
//     * LiveData支持了lifecycle生命周期检测
//     * @return
//     */
//    public LiveData<T> getLiveObservableData() {
//        return liveObservableData;
//    }
//
//    /**
//     * 当主动改变数据时重新设置被观察的数据
//     * @param product
//     */
//    public void setUiObservableData(T product) {
//        this.uiObservableData.set(product);
//    }
//
//    public Class<T> getTClass(){
//        Class<T> tClass = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
//        return tClass;
//    }

    @Override
    protected void onCleared() {
        super.onCleared();
        RetrofitManager.remove(this.getClass().getSimpleName());
        LogUtils.d("=======BaseViewModel--onCleared=========");
    }

    /**
     * 获取进度弹窗
     */
    public LoadingDialog getLoadingDialog(boolean cancelable) {
        LoadingDialog loadingDialog = null;
        Activity activity = ViewManager.getInstance().currentActivity();
        if (activity != null && !activity.isFinishing()) {
            loadingDialog = new LoadingDialog(activity, cancelable);
        }
        return loadingDialog;
    }
}
