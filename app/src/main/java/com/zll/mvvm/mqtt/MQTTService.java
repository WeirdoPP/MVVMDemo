package com.zll.mvvm.mqtt;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.apkfuns.logutils.LogUtils;
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
 */
public class MQTTService extends Service {

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

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.d("onCreate");
        init();
    }

    /**
     * 初始化
     */
    private void init() {
//        // 服务器地址（协议+地址+端口号）
//        client = new MqttAndroidClient(this, MQTTConstant.BROKER, MQTTConstant.CLIENT_ID);
//        // 设置MQTT监听并且接受消息
//        client.setCallback(mqttCallback);
//
//        conOpt = new MqttConnectOptions();
//        // 清除缓存
//        conOpt.setCleanSession(true);
//        // 设置超时时间，单位：秒
//        conOpt.setConnectionTimeout(10);
//        // 心跳包发送间隔，单位：秒
//        conOpt.setKeepAliveInterval(20);
//        // 用户名
//        conOpt.setUserName(MQTTConstant.ACESSKEY);
//        // 密码
//        conOpt.setPassword(MQTTConstant.PASSWORD.toCharArray());     //将字符串转换为字符串数组
//
//        // last will message
//        boolean doConnect = true;
//        String message = "{\"terminal_uid\":\"" + MQTTConstant.CLIENT_ID + "\"}";
//        LogUtils.d("message是: %s", message);
//        String TOPIC = MQTTConstant.TOPIC;
//        Integer qos = 0;
//        Boolean retained = false;
//        if ((!message.equals("")) || (!TOPIC.equals(""))) {
//            // 最后的遗嘱
//            // MQTT本身就是为信号不稳定的网络设计的，所以难免一些客户端会无故的和Broker断开连接。
//            //当客户端连接到Broker时，可以指定LWT，Broker会定期检测客户端是否有异常。
//            //当客户端异常掉线时，Broker就往连接时指定的topic里推送当时指定的LWT消息。
//
//            try {
//                conOpt.setWill(TOPIC, message.getBytes(), qos.intValue(), retained.booleanValue());
//            } catch (Exception e) {
//                LogUtils.d("Exception Occured %s", e);
//                doConnect = false;
//                iMqttActionListener.onFailure(null, e);
//            }
//        }
//
//        if (doConnect) {
//            doClientConnection();
//        }

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
                    if (IGetMessageCallBack != null) {
                        IGetMessageCallBack.setMessage(new String(mqttMessage.getPayload()));
                    }
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
     * 连接MQTT服务器
     */
//    private void doClientConnection(MqttConnectOptions connOpts) {
//        if (!sampleClient.isConnected() && isConnectIsNormal()) {
//            try {
//                sampleClient.connect(connOpts, null, iMqttActionListener);
//            } catch (MqttException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }

    /**
     * MQTT是否连接成功回调
     */
//    private IMqttActionListener iMqttActionListener = new IMqttActionListener() {
//
//        @Override
//        public void onSuccess(IMqttToken arg0) {
//            LogUtils.d("连接成功 ");
//            try {
//                // 订阅myTopic话题
//                sampleClient.subscribe(MQTTConstant.TOPIC, 1);
//            } catch (MqttException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void onFailure(IMqttToken arg0, Throwable arg1) {
//            arg1.printStackTrace();
//            // 连接失败，重连
//        }
//    };

    /**
     * MQTT监听并且接受消息回调
     */
//    private MqttCallback mqttCallback = new MqttCallback() {
//
//        @Override
//        public void messageArrived(String TOPIC, MqttMessage message) throws Exception {
//
//            String str1 = new String(message.getPayload());
//            if (IGetMessageCallBack != null) {
//                IGetMessageCallBack.setMessage(str1);
//            }
//            String str2 = TOPIC + ";qos:" + message.getQos() + ";retained:" + message.isRetained();
//            LogUtils.d("messageArrived:%s", str1);
//            LogUtils.d(str2);
//        }
//
//        @Override
//        public void deliveryComplete(IMqttDeliveryToken arg0) {
//
//        }
//
//        @Override
//        public void connectionLost(Throwable arg0) {
//            // 失去连接，重连
//        }
//    };
    @Override
    public void onDestroy() {
        stopSelf();
        try {
            sampleClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    /**
     * 判断网络是否连接
     */
//    private boolean isConnectIsNormal() {
//        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext()
//                .getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
//        if (info != null && info.isAvailable()) {
//            String name = info.getTypeName();
//            LogUtils.d("MQTT当前网络名称：%s", name);
//            return true;
//        } else {
//            LogUtils.d("MQTT 没有可用网络");
//            return false;
//        }
//    }
    @Override
    public IBinder onBind(Intent intent) {
        LogUtils.d("onBind");
        return new CustomBinder();
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
