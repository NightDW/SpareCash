package com.laidw.web.converter;

import com.laidw.entity.questionnaire.Type;
import org.springframework.core.convert.converter.Converter;

/**
 * 负责把用户提交的数字0/1/2转成相应的问题Type对象
 */
public class QueTypeConverter implements Converter<String, Type> {

    public Type convert(String str) {
        if("0".equals(str))
            return Type.SINGLE;
        if("1".equals(str))
            return Type.MULTI;
        return Type.ASKING;
    }
}
