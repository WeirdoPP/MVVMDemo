package com.zll.mvvm.viewmodel.mqtt;

import android.app.Application;
import android.support.annotation.NonNull;

import com.zll.mvvm.mqtt.MQTTService;

import google.architecture.common.base.BaseViewModel;

/**
 * @author zhanglianglin
 * @version 1.0
 * @desc
 * @since 2018/09/08
 */

public class TestMQTTViewModel extends BaseViewModel {

    public TestMQTTViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * 测试MQTT
     *
     * @param
     */
    public void testMQTT() {
        MQTTService.testPublish("测试MQTT");
    }
}
