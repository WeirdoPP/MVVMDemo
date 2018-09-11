package com.zll.mvvm.mqtt;


/**
 * @author Jewel
 * @version 1.0
 * @since 2017/7/20 0020
 */

public class MQTTConstant {
//
//    aliware.mqtt.GROUPID=GID_Lizikj_MqttMsgGroup_dev
//    aliware.mqtt.brokerUrl=post-cn-v0h0rwihv01.mqtt.aliyuncs.com
//    aliware.mqtt.producerId=PID_Lizikj_MqttMsgProducerGroup_dev
//    aliware.mqtt.consumerId=CID_Lizikj_MqttMsgConsumerGroup_dev
//    aliware.mqtt.TOPIC=LIZIKJ_MQTT_MSG_DEV


    //设置当前用户私有的MQTT的接入点。例如此处示意使用XXX，实际使用请替换用户自己的接入点。接入点的获取方法是，在控制台申请MQTT实例，每个实例都会分配一个接入点域名。
    public static final String BROKER = "tcp://post-cn-v0h0rwihv01.mqtt.aliyuncs.com:1883";
    //要订阅的主题
    public static final String TOPIC = "LIZIKJ_MQTT_MSG_DEV";
    /**
     * MQTT的ClientID，一般由两部分组成，GroupID@@@DeviceID
     * 其中GroupID在MQ控制台里申请
     * DeviceID由应用方设置，可能是设备编号等，需要唯一，否则服务端拒绝重复的ClientID连接
     */
    public static final String CLIENT_ID = "GID_Lizikj_MqttMsgGroup_dev@@@androidIds";//客户端标识
    /**
     * 设置阿里云的AccessKey，用于鉴权
     */
    public static final String ACESSKEY = "LTAIObtlCQ4TbmbR";
    /**
     * 设置阿里云的SecretKey，用于鉴权
     */
    public static final String SECRETKEY = "wluZR417bu2jmGoT3PQ4GckNvmtqBS";
    /**
     * GroupID在MQ控制台里申请
     */
    public static final String GROUPID = "GID_Lizikj_MqttMsgGroup_dev";


}
