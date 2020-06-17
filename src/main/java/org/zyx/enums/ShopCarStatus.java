package org.zyx.enums;

/**
 * 地址是否启用
 */
public enum ShopCarStatus {

    COOKIE_ADD(0,"购物车添加cookie成功"),
    CAR_ADD_SUCCESS(1,"购物车添加成功"),
    CAR_ADD_FAILED(2,"购物车添加失败"),
    ;

    private int type;
    private String msg;

    ShopCarStatus(int type, String msg) {
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
