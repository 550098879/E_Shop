package org.zyx.enums;

public enum SuperEnum {

    DOG(1,"狗狗"),
    CAT(2,"猫咪"),
    SMALL_PET(3,"小宠"),
    SUI(4,"水族"),
    BOTANY(6,"植物"),
    JBL(7,"宠物用品"),
    PET_SERVICE(8,"宠物服务"),
    ;

    private int type;
    private String msg;

    SuperEnum(int type, String msg) {
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
