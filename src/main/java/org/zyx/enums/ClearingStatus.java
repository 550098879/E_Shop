package org.zyx.enums;

/**
 * 地址是否启用
 */
public enum ClearingStatus {

    NOT_ENOUGH_MONEY(0,"余额不足"),
    NOT_ENOUGH_STOCK(1,"库存不足"),
    CLEARING_FILED(2,"结算失败"),
    CLEARING_SUCCESS(3,"结算成功"),
    ;

    private int type;
    private String msg;

    ClearingStatus(int type, String msg) {
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
