package org.zyx.enums;


/**
 * 管理员权限表
 */
public enum UsersRole {

    SUPER_ADMIN(1,"超级管理员"),
    COMMON_ADMIN(0,"普通管理员"),
    ;

    int type;
    String msg;

    UsersRole(int type, String msg) {
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
