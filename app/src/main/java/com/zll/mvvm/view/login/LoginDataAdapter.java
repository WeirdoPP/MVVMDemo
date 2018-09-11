package com.zll.mvvm.view.login;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zll.mvvm.R;
import com.zll.mvvm.constant.LoginDataEnum;

import java.util.List;

/**
 * @author zhanglianglin
 * @version 1.0
 * @desc 登录数据适配器
 * @since 2018/09/10
 */

public class LoginDataAdapter extends BaseQuickAdapter<LoginDataEnum, BaseViewHolder> {

    public LoginDataAdapter(@Nullable List<LoginDataEnum> data) {
        super(R.layout.item_login_data, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LoginDataEnum item) {
        helper.setText(R.id.tv_name, item.getType());
        helper.addOnClickListener(R.id.tv_name);
    }
}
