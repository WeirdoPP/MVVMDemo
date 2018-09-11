package com.zll.mvvm.view.login;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zll.mvvm.R;
import com.zll.mvvm.constant.LoginDataEnum;
import com.zll.mvvm.databinding.ActivityLoginBinding;
import com.zll.mvvm.viewmodel.login.LoginViewModel;

import java.util.Arrays;

import google.architecture.common.base.BaseActivity;
import google.architecture.coremodel.datamodel.http.entities.GirlsData;

/**
 * @author zhanglianglin
 * @version 1.0
 * @desc 登录页面
 * @since 2018/09/03
 */

public class LoginActivity extends BaseActivity<LoginViewModel>
        implements BaseQuickAdapter.OnItemChildClickListener {

    private ActivityLoginBinding loginBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        setViewModel(ViewModelProviders.of(LoginActivity.this).get(LoginViewModel.class));
        loginBinding.setLoginViewModel(getViewModel());

        LoginDataAdapter loginDataAdapter = new LoginDataAdapter(Arrays.asList(LoginDataEnum.values()));
        loginDataAdapter.setOnItemChildClickListener(this);
        loginBinding.recyclerView.setAdapter(loginDataAdapter);
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

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.tv_name:
                loginBinding.getLoginViewModel().gotoPage((LoginDataEnum) adapter.getData().get(position));
                break;
            default:
                break;
        }
    }
}
