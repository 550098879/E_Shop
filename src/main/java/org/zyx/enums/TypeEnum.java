package org.zyx.enums;

public enum TypeEnum {

    SuperType(0,"顶级分类"),
    ;

    private int type;
    private String msg;

    TypeEnum(int type, String msg) {
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
