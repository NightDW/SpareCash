package com.laidw.entity.task;

import com.laidw.entity.questionnaire.Questionnaire;
import lombok.Getter;
import lombok.Setter;

/**
 * 本类用于存储调查任务的数据
 */
@Getter@Setter
public class InvestigatingTask extends Task {
    /**
     * 要调查的学生的年级，格式为"m-n"，符合要求的学生才能接收该任务
     */
    private String gradeLimit;

    /**
     * 要调查的学生的性别，符合要求的学生才能接收该任务
     */
    private Integer genderLimit;

    /**
     * 要调查的学生的年龄，格式为"m-n"，符合要求的学生才能接收该任务
     */
    private String ageLimit;

    /**
     * 要调查的学生的专业，格式为"a,b,c"，符合要求的学生才能接收该任务
     */
    private String majorLimit;

    /**
     * 此次调查任务涉及到的要填写的调查问卷
     */
    private Questionnaire questionnaire;

    /**
     * 重写toString()方法
     * @return 转换成的字符串
     */
    public String toString() {
        return "Investigating" + super.toString() + "{" +
                "gradeLimit='" + gradeLimit + '\'' +
                ", genderLimit=" + genderLimit +
                ", ageLimit='" + ageLimit + '\'' +
                ", majorLimit='" + majorLimit + '\'' +
                ", questionnaire=" + questionnaire +
                '}';
    }
}
