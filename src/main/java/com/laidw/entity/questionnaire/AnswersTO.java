package com.laidw.entity.questionnaire;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 本实体类用于封装List&lt;Answer&gt;集合
 * 主要是为了方便SpringMVC封装用户提交的数据
 * 因为一个问卷往往有多个问题，用户就需要提交多个回答
 */
@Getter@Setter@ToString
public class AnswersTO {
    /**
     * Answer的集合
     */
    private List<Answer> answers;
}
