package org.zyx.enums;

/**
 * 管理员状态
 */
public enum UserStatus {

    START_USE(1,"启用"),
    STOP_USE(0,"禁用"),
    ;

    private int type;
    private String msg;

    UserStatus(int type, String msg) {
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
