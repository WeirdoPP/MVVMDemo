package com.zll.mvvm.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

import google.architecture.common.base.BaseViewModel;
import google.architecture.coremodel.datamodel.http.entities.GirlsData;
import google.architecture.coremodel.http.request.LoginRequest;
import google.architecture.coremodel.http.result.base.Response;
import google.architecture.coremodel.http.service.CommonHttp;
import google.architecture.coremodel.http.service.core.CallbackSuccess;

/**
 * @author zhanglianglin
 * @version 1.0
 * @desc 登录
 * @since 2018/09/03
 */

public class LoginViewModel extends BaseViewModel {

    //UI使用可观察的数据 ObservableField是一个包装类 用于更新
//    protected ObservableField<GirlsData> girlsDataObservableField = new ObservableField<>();
    public ObservableBoolean isCheck = new ObservableBoolean();
    //生命周期观察的数据
    protected MutableLiveData<GirlsData> girlsDataLiveData = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * LiveData支持了lifecycle生命周期检测
     *
     * @return
     */
    public LiveData<GirlsData> getLiveObservableData() {
        return girlsDataLiveData;
    }

//    /**
//     * 设置
//     * @param product
//     */
//    public void setUiObservableData(GirlsData product) {
//        this.girlsDataObservableField.set(product);
//    }

    /**
     * 登录
     */
    public void login(LoginRequest loginRequest) {
        isCheck.set(true);
        addHttpListener(CommonHttp.getGirlsDatas(getApplication(), getLoadingDialog(false),
                "20", "1", new CallbackSuccess<Response<GirlsData>>() {
                    @Override
                    public void handlerSuccess(Response<GirlsData> data) {
                        if (data.getData() != null) {
                            girlsDataLiveData.setValue(data.getData());
                        }
                    }
                }));
    }
}
