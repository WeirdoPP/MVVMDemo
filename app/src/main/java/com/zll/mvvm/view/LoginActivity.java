package com.zll.mvvm.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.apkfuns.logutils.LogUtils;
import com.zll.mvvm.R;
import com.zll.mvvm.databinding.ActivityLoginBinding;
import com.zll.mvvm.viewmodel.LoginViewModel;

import google.architecture.common.base.BaseActivity;
import google.architecture.coremodel.datamodel.http.entities.GirlsData;
import google.architecture.coremodel.http.request.LoginRequest;

/**
 * @author zhanglianglin
 * @version 1.0
 * @desc 登录页面
 * @since 2018/09/03
 */

public class LoginActivity extends BaseActivity<LoginViewModel> {

    private ActivityLoginBinding loginBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        setViewModel(ViewModelProviders.of(LoginActivity.this).get(LoginViewModel.class));
        loginBinding.setLoginViewModel(getViewModel());
        loginBinding.setLoginRequest(new LoginRequest("登录"));
        subscribeToModel(getViewModel());
    }

    /**
     * 订阅数据变化来刷新UI
     *
     * @param model
     */
    private void subscribeToModel(final LoginViewModel model) {
        //观察数据变化来刷新UI
        model.getLiveObservableData().observe(this, new Observer<GirlsData>() {
            @Override
            public void onChanged(@Nullable GirlsData girlsData) {
                LogUtils.d("刷新UI数据");
//                model.setUiObservableData(girlsData);
//                loginBinding.tvLogin.setText(String.valueOf(girlsData.isError()));
            }
        });
    }

}
