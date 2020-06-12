package org.zyx.enums;

/**
 * 注册状态判断
 */
public enum RegStatus {

    ACCOUNT_EXIST(0,"账户已存在"),
    REG_FAILED(1,"注册失败"),
    REG_SUCCEED(2,"注册成功"),
    ;

    private int type;
    private String msg;

    RegStatus(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }

}
