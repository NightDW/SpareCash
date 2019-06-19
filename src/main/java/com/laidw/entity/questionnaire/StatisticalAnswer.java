package com.laidw.entity.questionnaire;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 本类用于存储调查问卷的回答的统计信息
 * 注意数据库不会为本实体类建立一个对应的表，也就是说本类对象的数据不会直接存储到表中
 * 一份调查问卷的回答的数据将会以List&lt;StatisticalAnswer&gt;的JSON数据的形式存到数据库中
 */
@Getter@Setter@ToString
public class StatisticalAnswer {
    /**
     * 该统计结果的编号，用于表示该统计结果是第几题的统计结果
     */
    private Integer num;

    /**
     * 该统计结果的类型，和问题的类型相对应
     */
    private Type type;

    /**
     * 该统计结果统计了多少个用户的提交的答案
     */
    private Integer amount;

    /**
     * 用于统计单选题或多选题的结果，如果第一个元素为3，说明有3人选了第一个选项
     * 注意单选题和多选题最多只有8个选项，因此该集合大小最大为8
     */
    private List<Integer> chooseResults;

    /**
     * 用于统计问答题的结果，假设有2个用户分别回答"OK"和"FINE"
     * 那么该集合就有两个元素，分别为"OK"和"FINE"
     */
    private List<String> askResults;

    /**
     * 无参构造器
     */
    @SuppressWarnings("WeakerAccess")
    public StatisticalAnswer(){
        this.setNum(null);
        this.setType(null);
        this.setAmount(0);
        this.setChooseResults(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0));
        this.setAskResults(new ArrayList<>());
    }

    /**
     * 有参构造器
     * @param num 指定该统计结果对应的题目的编号
     * @param type 指定该统计结果对应的题目的类型
     */
    public StatisticalAnswer(Integer num, Type type){
        this();
        this.setNum(num);
        this.setType(type);
    }

    /**
     * 把一个答案统计进去
     * @param answer 要统计的答案
     * @return 把统计后的结果返回回去
     */
    public StatisticalAnswer addAnswer(Answer answer){
        if(this.num == null || this.type == null)
            throw new RuntimeException("未指定统计结果的编号或类型！");
        if(this.num.intValue() != answer.getNum() || this.type != answer.getType())
            throw new RuntimeException("题目编号或题目类型不对应！");

        this.amount++;

        //如果是问答题
        if(this.type.equals(Type.ASKING)) {
            //如果提交的答案为空，则直接返回原来的统计结果；注意这一步要在this.amount++之前
            //因为选填的问题的提交结果就有可能为空，而该结果显然算是总结果的其中一个
            if(answer.getAnswer() == null || answer.getAnswer().isEmpty())
                return this;
            this.askResults.add(answer.getAnswer());
        }
        //如果是单选或多选题
        else{
            //同样，如果提交的答案为空，则直接把原来的统计结果返回
            if(answer.getSelectedOpts() == null)
                return this;
            List<Integer> opts = answer.getSelectedOpts();
            for(Integer opt : opts)
                if (opt != null)
                   this.chooseResults.set(opt - 1, this.getChooseResults().get(opt - 1) + 1);
        }
        //把本对象返回回去，方便链式调用
        return this;
    }
}
