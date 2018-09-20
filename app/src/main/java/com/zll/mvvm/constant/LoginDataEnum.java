package com.zll.mvvm.constant;

/**
 * @author zhanglianglin
 * @version 1.0
 * @desc
 * @since 2018/09/10
 */

public enum LoginDataEnum {

    TEST_NET_CACHE("测试网络缓存");

    private String type;

    LoginDataEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
