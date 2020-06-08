package org.zyx.VO;

import lombok.Data;

import java.util.Map;

@Data
public class MapVO {
    private int code;
    private String msg;
    private Map data;
}
