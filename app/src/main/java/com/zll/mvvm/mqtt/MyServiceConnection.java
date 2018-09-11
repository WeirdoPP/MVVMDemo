package com.zll.mvvm.mqtt;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * @author zhanglianglin
 * @version 1.0
 * @desc 为了实现通过回调{@link IGetMessageCallBack}去传递从服务端获取到的消息，
 * 实现一个ServiceConnection类，并且通过onBind来从Service和Activity之间传递数据
 * @since 2018/09/08
 */
public class MyServiceConnection implements ServiceConnection {

    private MQTTService mqttService;
    private IGetMessageCallBack IGetMessageCallBack;

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mqttService = ((MQTTService.CustomBinder) iBinder).getService();
        mqttService.setIGetMessageCallBack(IGetMessageCallBack);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

    public MQTTService getMqttService() {
        return mqttService;
    }

    public void setIGetMessageCallBack(IGetMessageCallBack IGetMessageCallBack) {
        this.IGetMessageCallBack = IGetMessageCallBack;
    }
}
