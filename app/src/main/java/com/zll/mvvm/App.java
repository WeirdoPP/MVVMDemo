package com.zll.mvvm;

import com.alibaba.android.arouter.launcher.ARouter;
import com.facebook.drawee.backends.pipeline.Fresco;

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
        ARouter.init(this);
        //room初始化
        RoomManager.initRoom(this);
    }
}
