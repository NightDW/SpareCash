package com.laidw.web.converter;

import com.laidw.entity.user.Identity;
import org.springframework.core.convert.converter.Converter;

/**
 * 负责把用户提交的数字0/1转成相应的Identity对象
 */
public class IdentityConverter implements Converter<String, Identity> {

    public Identity convert(String str) {
        if("0".equals(str))
            return Identity.STUDENT;
        return Identity.COW;
    }
}
