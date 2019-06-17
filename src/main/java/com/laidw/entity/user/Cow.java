package com.laidw.entity.user;

import lombok.Getter;
import lombok.Setter;

/**
 * 本类用于存储机构，即奶牛的数据
 */
@Getter@Setter
public class Cow extends User{
    /**
     * 重写toString()方法
     * @return 转换成的字符串
     */
    public String toString(){
        return "Cow" + super.toString();
    }
}
