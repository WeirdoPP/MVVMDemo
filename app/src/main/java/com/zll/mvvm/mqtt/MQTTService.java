package com.zll.mvvm.mqtt;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.apkfuns.logutils.LogUtils;
import com.xdandroid.hellodaemon.AbsWorkService;
import com.zll.mvvm.R;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static org.eclipse.paho.client.mqttv3.MqttConnectOptions.MQTT_VERSION_3_1_1;

/**
 * @author zhanglianglin
 * @version 1.0
 * @desc mqtt服务
 * @since 2018/09/08
 * <p>
 * 服务既可以是启动服务，也允许绑定。此时需要同时实现以下回调方法：(startService)onStartCommand()和 (bindService)onBind()。系统不会在所有客户端都取消绑定时销毁服务。为此，必须通过调用 stopSelf() 或 stopService() 显式停止服务
 * 通过添加 android:exported 属性并将其设置为 "false"，确保服务仅适用于本应用
 * <p>
 * https://www.jianshu.com/p/95ec2a23f300
 */
public class MQTTService extends AbsWorkService {

    private static MqttAndroidClient sampleClient;


    private IGetMessageCallBack IGetMessageCallBack;

    /**
     * 推送一个消息,用于测试
     *
     * @param msg
     */
    public static void testPublish(String msg) {
        String topic = MQTTConstant.TOPIC;
        Integer qos = 0;
        Boolean retained = false;
        try {
            if (sampleClient != null) {
                sampleClient.publish(topic, msg.getBytes(), qos.intValue(), retained.booleanValue());
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化
     */
    private void init() {
        String sign;
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            sampleClient = new MqttAndroidClient(getApplication(), MQTTConstant.BROKER, MQTTConstant.CLIENT_ID, persistence, MqttAndroidClient.Ack.AUTO_ACK);
            final MqttConnectOptions connOpts = new MqttConnectOptions();
            LogUtils.d("Connecting to BROKER: " + MQTTConstant.BROKER);
            /**
             * 计算签名，将签名作为MQTT的password
             * 签名的计算方法，参考工具类MacSignature，第一个参数是ClientID的前半部分，即GroupID
             * 第二个参数阿里云的SecretKey
             */
            sign = MacSignature.macSignature(MQTTConstant.GROUPID, MQTTConstant.SECRETKEY);
            /**
             * 设置订阅方订阅的Topic集合，此处遵循MQTT的订阅规则，可以是一级Topic，二级Topic，P2P消息请订阅/p2p
             */
            final String[] topicFilters = new String[]{MQTTConstant.TOPIC + "/p2p"};
            final int[] qos = {1};
            // 设置超时时间，单位：秒
            connOpts.setConnectionTimeout(10);
            connOpts.setUserName(MQTTConstant.ACESSKEY);
            connOpts.setServerURIs(new String[]{MQTTConstant.BROKER});
            connOpts.setPassword(sign.toCharArray());
            connOpts.setCleanSession(true);
            connOpts.setKeepAliveInterval(90);
            connOpts.setMqttVersion(MQTT_VERSION_3_1_1);
            connOpts.setAutomaticReconnect(true);
            sampleClient.setCallback(new MqttCallbackExtended() {
                public void connectComplete(boolean reconnect, String serverURI) {
                    LogUtils.d("connect success url %s", serverURI);
                    //连接成功，需要上传客户端所有的订阅关系
                    try {
                        sampleClient.subscribe(topicFilters, qos);
                    } catch (MqttException e) {
                        e.printStackTrace();
                        LogUtils.e("订阅失败");
                    }
                }

                public void connectionLost(Throwable throwable) {
                    LogUtils.e("mqtt connection lost");
                }

                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                    LogUtils.d("messageArrived:" + topic + "------" + new String(mqttMessage.getPayload()));

                }

                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    LogUtils.d("deliveryComplete:" + iMqttDeliveryToken.getMessageId());
                }
            });
            Flowable.just(1).observeOn(Schedulers.io())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) throws Exception {
                            //客户端每次上线都必须上传自己所有涉及的订阅关系，否则可能会导致消息接收延迟
                            try {
//                                sampleClient.setManualAcks();
                                sampleClient.connect(connOpts);
//                                //每个客户端最多允许存在30个订阅关系，超出限制可能会丢弃导致收不到部分消息
//                                sampleClient.subscribe(topicFilters, qos);
                            } catch (MqttException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (Exception me) {
            me.printStackTrace();
        }

    }

    /**
     * 在服务第一次创建的时候调用
     */
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.d("onCreate");

    }

    /**
     * 是否 任务完成, 不再需要服务运行?
     *
     * @return 应当停止服务, true; 应当启动服务, false; 无法判断, null.
     */
    @Override
    public Boolean shouldStopService(Intent intent, int flags, int startId) {
        return false;
    }

    @Override
    public void startWork(Intent intent, int flags, int startId) {
        init();
    }

    @Override
    public void stopWork(Intent intent, int flags, int startId) {
        stopSelf(startId);
        try {
            sampleClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 任务是否正在运行?
     *
     * @return 任务正在运行, true; 任务当前不在运行, false; 无法判断, null.
     */
    @Override
    public Boolean isWorkRunning(Intent intent, int flags, int startId) {
        return sampleClient != null && sampleClient.isConnected();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent, Void alwaysNull) {
        LogUtils.d("onBind");
        return null;
    }

    /**
     * 服务被杀时调用, 可以在这里面保存数据.
     *
     * @param rootIntent
     */
    @Override
    public void onServiceKilled(Intent rootIntent) {

    }

    /**
     * 每次服务启动的时候调用,我们如果一旦服务启动就需要去执行某些操作，逻辑就可以写在 onStartCommand() 方法中
     * <p>
     * 每次回调onStartCommand()方法时，参数“startId”的值都是递增的，startId用于唯一标识每次对Service发起的处理请求
     * 如果服务同时处理多个 onStartCommand() 请求，则不应在处理完一个启动请求之后立即销毁服务，
     * 因为此时可能已经收到了新的启动请求，在第一个请求结束时停止服务会导致第二个请求被终止。
     * 为了避免这一问题，可以使用 stopSelf(int) 确保服务停止请求始终基于最新一次的启动请求。
     * 也就是说，如果调用 stopSelf(int) 方法的参数值与onStartCommand()接受到的最新的startId值不相符的话，
     * stopSelf()方法就会失效，从而避免终止尚未处理的请求
     * <p>
     * onStartCommand() 方法必须返回一个整数，用于描述系统应该如何应对服务被杀死的情况，返回值必须是以下常量之一：
     * START_NOT_STICKY
     * 如果系统在 onStartCommand() 返回后终止服务，则除非有挂起 Intent 要传递，否则系统不会重建服务。这是最安全的选项，可以避免在不必要时以及应用能够轻松重启所有未完成的作业时运行服务
     * START_STICKY
     * 如果系统在 onStartCommand() 返回后终止服务，则会重建服务并调用 onStartCommand()，但不会重新传递最后一个 Intent。相反，除非有挂起 Intent 要启动服务（在这种情况下，将传递这些 Intent ），否则系统会通过空 Intent 调用 onStartCommand()。这适用于不执行命令、但无限期运行并等待作业的媒体播放器（或类似服务）
     * START_REDELIVER_INTENT
     * 如果系统在 onStartCommand() 返回后终止服务，则会重建服务，并通过传递给服务的最后一个 Intent 调用 onStartCommand()。任何挂起 Intent 均依次传递。这适用于主动执行应该立即恢复的作业（例如下载文件）的服务
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.d("onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 销毁服务时调用
     * 在这里可以回收不在使用的资源
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d("onDestroy");
    }

    /**
     * 设置客户端接收消息回调
     *
     * @param IGetMessageCallBack
     */
    public void setIGetMessageCallBack(IGetMessageCallBack IGetMessageCallBack) {
        this.IGetMessageCallBack = IGetMessageCallBack;
    }

    /**
     * 创建一个通知
     *
     * @param message
     */
    public void toCreateNotification(String message) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(this, MQTTService.class), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);//3、创建一个通知，属性太多，使用构造器模式

        Notification notification = builder
                .setTicker("测试标题")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("")
                .setContentText(message)
                .setContentInfo("")
                .setContentIntent(pendingIntent)//点击后才触发的意图，“挂起的”意图
                .setAutoCancel(true)        //设置点击之后notification消失
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        startForeground(0, notification);
        notificationManager.notify(0, notification);

    }

    /**
     * 自定义binder
     */
    public class CustomBinder extends Binder {
        public MQTTService getService() {
            return MQTTService.this;
        }
    }
}
