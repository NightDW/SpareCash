package com.laidw.web.converter;

import com.laidw.entity.task.Type;
import org.springframework.core.convert.converter.Converter;

/**
 * 负责把用户提交的数字0/1转成相应的任务Type对象
 */
public class TaskTypeConverter implements Converter<String, Type> {

    public Type convert(String str) {
        if("0".equals(str))
            return Type.NORMAL;
        return Type.INVESTIGATING;
    }
}
