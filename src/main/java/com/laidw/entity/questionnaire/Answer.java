package com.laidw.entity.questionnaire;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 本类用于存储调查问卷的答案的信息
 * 注意一个问题会有多个用户回答，我们显然不可能把各个用户对该问题的回答都存储起来
 * 而是应该把各个用户对该问题的回答的统计结果存储起来
 * 因此本类仅仅用于封装用户在表单填写的数据，并提交给Controller
 * Controller拿到里面的数据来更新该问卷的StatisticalAnswer数据
 */
@Getter@Setter@ToString
public class Answer {
    /**
     * 该答案的编号，用来表示该答案是第几题的答案
     */
    private Integer num;

    /**
     * 该答案的类型，和问题的类型相对应
     */
    private Type type;

    /**
     * 该答案的内容，只用于问答题，整个字符串就是用户的回答
     */
    private String answer;

    /**
     * 被选中的选项，只用于单选题和多选题
     * 该集合最多有8个元素，若该集合有1 2这两个元素，则说明用户选择了第一和第二个选项
     * 提供这个属性是为了方便SpringMVC处理表单的数据
     */
    private List<Integer> selectedOpts;
}
