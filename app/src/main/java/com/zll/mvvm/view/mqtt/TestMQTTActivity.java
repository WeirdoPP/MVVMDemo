package com.zll.mvvm.view.mqtt;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.apkfuns.logutils.LogUtils;
import com.zll.mvvm.R;
import com.zll.mvvm.constant.RoutePathConstant;
import com.zll.mvvm.databinding.ActivityTestMqttBinding;
import com.zll.mvvm.mqtt.IGetMessageCallBack;
import com.zll.mvvm.mqtt.MQTTService;
import com.zll.mvvm.mqtt.MyServiceConnection;
import com.zll.mvvm.viewmodel.mqtt.TestMQTTViewModel;

import google.architecture.common.base.BaseActivity;

/**
 * @author zhanglianglin
 * @version 1.0
 * @desc 测试阿里云MQTT
 * @since 2018/09/08
 */
@Route(path = RoutePathConstant.TEST_MQTT)
public class TestMQTTActivity extends BaseActivity<TestMQTTViewModel> implements IGetMessageCallBack {

    private ActivityTestMqttBinding activityTestMqttBinding;
    private MyServiceConnection myServiceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myServiceConnection = new MyServiceConnection();
        myServiceConnection.setIGetMessageCallBack(this);

        Intent intent = new Intent(this, MQTTService.class);
        bindService(intent, myServiceConnection, Context.BIND_AUTO_CREATE);

        activityTestMqttBinding = DataBindingUtil.setContentView(this, R.layout.activity_test_mqtt);
        setViewModel(ViewModelProviders.of(this).get(TestMQTTViewModel.class));
        activityTestMqttBinding.setTestMQTTViewModel(getViewModel());
    }

    @Override
    public void setMessage(String message) {
        LogUtils.d("MQTT 接收到的消息 %s", message);
        myServiceConnection.getMqttService().toCreateNotification(message);
    }

    @Override
    protected void onDestroy() {
        unbindService(myServiceConnection);
        super.onDestroy();
    }
}
