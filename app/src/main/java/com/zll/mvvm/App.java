package com.zll.mvvm;

import android.content.Intent;

import com.alibaba.android.arouter.launcher.ARouter;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.mvvm.opensoure.AppRootFileUtils;
import com.xdandroid.hellodaemon.DaemonEnv;
import com.zll.mvvm.mqtt.MQTTService;

import google.architecture.common.base.BaseApplication;
import google.architecture.common.util.Utils;
import google.architecture.coremodel.room.RoomManager;

/**
 * Created by dxx on 2017/11/13.
 */

public class App extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        //毕加索初始化
        Fresco.initialize(this);
        //路由初始化
        if (Utils.isAppDebug()) {
            //开启InstantRun之后，一定要在ARouter.init之前调用openDebug
            ARouter.openDebug();
            ARouter.openLog();
        }
        //路由初始化
        ARouter.init(this);
        //room初始化
        RoomManager.initRoom(this);
        //常用工具类初始化
        Utils.init(this);
        //APP 文件目录初始化
        AppRootFileUtils.onCreate(getApplicationContext());
        //守护进程初始化,启动服务
        DaemonEnv.initialize(this, MQTTService.class, null);
        startService(new Intent(this, MQTTService.class));

    }

}
