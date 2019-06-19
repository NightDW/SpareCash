package com.laidw.entity.questionnaire;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 本类用于存储调查问卷的问题的信息
 * 注意数据库不会为本实体类建立一个对应的表，也就是说本类对象的数据不会直接存储到表中
 * 一份调查问卷的问题数据将会以List&lt;Question&gt;的JSON数据的形式存到数据库中
 */
@Getter@Setter@ToString
public class Question {
    /**
     * 该问题的编号，用来表示该问题是第几题
     */
    private Integer num;

    /**
     * 该问题的类型，如单选题，多选题，问答题
     */
    private Type type;

    /**
     * 该问题的题目，如"你的兴趣是什么？"等
     */
    private String title;

    /**
     * 该问题的选项，如"听音乐"，"打篮球"等，最多有8个选项
     */
    private List<String> options;

    /**
     * 该问题是否为必填
     */
    private Boolean isRequired;
}
