package com.laidw.config;

import com.laidw.web.converter.IdentityConverter;
import com.laidw.web.converter.QueTypeConverter;
import com.laidw.web.converter.TaskTypeConverter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

/**
 * 用于注册3个类型转换器
 */
@Configuration
public class MyConfig {

    @Bean
    public Converter getIdentityConverter(){
        return new IdentityConverter();
    }

    @Bean
    public Converter getQueTypeConverter(){
        return new QueTypeConverter();
    }

    @Bean
    public Converter getTaskTypeConverter(){
        return new TaskTypeConverter();
    }
}
