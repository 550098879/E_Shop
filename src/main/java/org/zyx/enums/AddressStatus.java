package org.zyx.enums;

/**
 * 地址是否启用
 */
public enum AddressStatus {

    START_USE(1,"启用"),
    STOP_USE(0,"禁用"),
    ;

    private int type;
    private String msg;

    AddressStatus(int type, String msg) {
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
