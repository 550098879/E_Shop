package org.zyx.enums;

/**
 * 订单状态
 */
public enum OrderStatus {

    CREATE(0,"创建"),
    SEND_OUT(1,"已发货"),
    COMPLETE(2,"订单完成"),
    ;

    private int type;
    private String msg;

    OrderStatus(int type, String msg) {
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
