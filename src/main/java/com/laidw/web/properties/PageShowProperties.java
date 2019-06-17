package com.laidw.web.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 本类用于封装Controller需要使用到的一些数据
 * 主要是和展示页面相关的；如一个页面要展示几条数据等
 */

@Component
@Getter @Setter @ToString
@ConfigurationProperties(prefix = "controller.page")
public class PageShowProperties {
    /**
     * 指定一个页面要展示多少条属性数据
     */
    private Integer pageSize = 100;

    /**
     * 指定导航条上要显示几个页码
     */
    private Integer navigatePages = 7;
}
