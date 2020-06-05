package org.zyx.enums;

/**
 * 商品是否上架
 */
public enum GoodsStatus {

    PUT_AWAY(1,"已上架"),
    SOLD_OUT(0,"未上架"),
    ;

    private int type;
    private String msg;

    GoodsStatus(int type, String msg) {
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
