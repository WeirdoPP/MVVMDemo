//package com.zll.mvvm.mqtt;
//
//import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
//import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
//import org.eclipse.paho.client.mqttv3.MqttClient;
//import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
//import org.eclipse.paho.client.mqttv3.MqttMessage;
//import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
//
//import java.io.IOException;
//
//import static org.eclipse.paho.client.mqttv3.MqttConnectOptions.MQTT_VERSION_3_1_1;
//
///**
// * Created by alvin on 16-6-24.
// * 接收
// */
//public class MqttRecvDemo {
//    public static void main(String[] args) throws IOException {
//        /**
//         * 设置当前用户私有的MQTT的接入点。例如此处示意使用XXX，实际使用请替换用户自己的接入点。接入点的获取方法是，在控制台申请MQTT实例，每个实例都会分配一个接入点域名。
//         */
//        final String BROKER = "tcp://XXXX.mqtt.aliyuncs.com:1883";
//        /**
//         * 设置阿里云的AccessKey，用于鉴权
//         */
//        final String ACESSKEY = "XXXXXX";
//        /**
//         * 设置阿里云的SecretKey，用于鉴权
//         */
//        final String SECRETKEY = "XXXXXXX";
//        /**
//         * 发消息使用的一级Topic，需要先在MQ控制台里申请
//         */
//        final String TOPIC = "XXXX";
//        /**
//         * MQTT的ClientID，一般由两部分组成，GroupID@@@DeviceID
//         * 其中GroupID在MQ控制台里申请
//         * DeviceID由应用方设置，可能是设备编号等，需要唯一，否则服务端拒绝重复的ClientID连接
//         */
//        final String clientId = "GID_XXXX@@@ClientID_XXXXXX";
//        String sign;
//        MemoryPersistence persistence = new MemoryPersistence();
//        try {
//            final MqttClient sampleClient = new MqttClient(BROKER, clientId, persistence);
//            final MqttConnectOptions connOpts = new MqttConnectOptions();
//            System.out.println("Connecting to BROKER: " + BROKER);
//            /**
//             * 计算签名，将签名作为MQTT的password
//             * 签名的计算方法，参考工具类MacSignature，第一个参数是ClientID的前半部分，即GroupID
//             * 第二个参数阿里云的SecretKey
//             */
//            sign = MacSignature.macSignature(clientId.split("@@@")[0], SECRETKEY);
//            /**
//             * 设置订阅方订阅的Topic集合，此处遵循MQTT的订阅规则，可以是一级Topic，二级Topic，P2P消息请订阅/p2p
//             */
//            final String[] topicFilters = new String[]{TOPIC + "/notice/", TOPIC + "/p2p"};
//            final int[] qos = {0, 0};
//            connOpts.setUserName(ACESSKEY);
//            connOpts.setServerURIs(new String[]{BROKER});
//            connOpts.setPassword(sign.toCharArray());
//            connOpts.setCleanSession(true);
//            connOpts.setKeepAliveInterval(90);
//            connOpts.setMqttVersion(MQTT_VERSION_3_1_1);
//            connOpts.setAutomaticReconnect(true);
//            sampleClient.setCallback(new MqttCallbackExtended() {
//                public void connectComplete(boolean reconnect, String serverURI) {
//                    System.out.println("connect success");
//                    //连接成功，需要上传客户端所有的订阅关系
//                    sampleClient.subscribe(topicFilters, qos);
//                }
//
//                public void connectionLost(Throwable throwable) {
//                    System.out.println("mqtt connection lost");
//                }
//
//                public void messageArrived(String TOPIC, MqttMessage mqttMessage) throws Exception {
//                    System.out.println("messageArrived:" + TOPIC + "------" + new String(mqttMessage.getPayload()));
//                }
//
//                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
//                    System.out.println("deliveryComplete:" + iMqttDeliveryToken.getMessageId());
//                }
//            });
//            //客户端每次上线都必须上传自己所有涉及的订阅关系，否则可能会导致消息接收延迟
//            sampleClient.connect(connOpts);
//            //每个客户端最多允许存在30个订阅关系，超出限制可能会丢弃导致收不到部分消息
//            sampleClient.subscribe(topicFilters, qos);
//            Thread.sleep(Integer.MAX_VALUE);
//        } catch (Exception me) {
//            me.printStackTrace();
//        }
//    }
//}
