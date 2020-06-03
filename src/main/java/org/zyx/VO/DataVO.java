package org.zyx.VO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataVO<T> {
    private int code;
    private String msg;
    private int count;
    private Collection<T> data;
}
