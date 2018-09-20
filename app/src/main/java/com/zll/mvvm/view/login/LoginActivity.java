package com.zll.mvvm.view.login;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yanzhenjie.permission.AndPermission;
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

        requestPermissions();

        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        setViewModel(ViewModelProviders.of(LoginActivity.this).get(LoginViewModel.class));
        loginBinding.setLoginViewModel(getViewModel());

        LoginDataAdapter loginDataAdapter = new LoginDataAdapter(Arrays.asList(LoginDataEnum.values()));
        loginDataAdapter.setOnItemChildClickListener(this);
        loginBinding.recyclerView.setAdapter(loginDataAdapter);
    }

    /**
     * 权限请求
     */
    public void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            AndPermission.with(this)
                    .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    .rationale((context, permissions, executor) -> {
//                        因此Rationale功能是在用户拒绝一次权限后，再次申请时检测到已经申请过一次该权限了，
//                        允许开发者弹窗说明申请权限的目的，获取用户的同意后再申请权限，
//                        避免用户勾选不再提示，导致不能再次申请权限。
                        LogUtils.d("在这填写拒绝权限的说明,获取用户的同意后再申请权限");
                    })
                    .onGranted(permissions -> {
//                        //获得权限
                        LogUtils.e("通过了全部权限");
                    }).onDenied(permissions -> {
                if (AndPermission.hasAlwaysDeniedPermission(LoginActivity.this, permissions)) {
                    // 这些权限被用户总是拒绝。
                    LogUtils.e("这些权限被用户总是拒绝");
                } else {
                    //拒绝权限
                    LogUtils.e("拒绝了部分权限");
                }
            }).start();
        }
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
