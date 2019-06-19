package com.laidw.entity.task;

import lombok.Getter;
import lombok.Setter;

/**
 * 本类用于存储普通任务的数据
 */
@Getter@Setter
public class NormalTask extends Task {
    /**
     * 重写toString()方法
     * @return 转换成的字符串
     */
    public String toString(){
        return "Normal" + super.toString();
    }
}
