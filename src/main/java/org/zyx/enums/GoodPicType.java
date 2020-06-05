package org.zyx.enums;

/**
 * 商品图片类型
 */

public enum GoodPicType {

    cover(0, "封面"),
    others(1,"其他");
    ;

    private int type;
    private String msg;

    GoodPicType(int type, String msg) {
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
