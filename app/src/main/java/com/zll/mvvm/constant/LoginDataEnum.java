package com.zll.mvvm.constant;

/**
 * @author zhanglianglin
 * @version 1.0
 * @desc
 * @since 2018/09/10
 */

public enum LoginDataEnum {

    ;

    private String type;

    LoginDataEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
