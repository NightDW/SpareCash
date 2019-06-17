package com.laidw.entity.pagebean;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import lombok.*;

import java.util.List;

/**
 * 和网页展示相关的一个实体类
 * @param <T> T是什么类型，就表示网页要展示什么数据
 */

@Getter@Setter@ToString@NoArgsConstructor@AllArgsConstructor
public class PageBean<T> {
    /**
     * 用于存放要展示的数据
     */
    private List<T> list;

    /**
     * 用于存放分页的信息
     */
    private Page<T> page;

    /**
     * 用于存放更详细的分页信息
     */
    private PageInfo<T> pageInfo;
}
