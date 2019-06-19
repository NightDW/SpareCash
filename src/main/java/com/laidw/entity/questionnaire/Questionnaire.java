package com.laidw.entity.questionnaire;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.IOException;
import java.util.List;

/**
 * 本类用于存储整份调查问卷的信息
 * 注意，本类对象在数据库的存储方式比较特殊
 * 在数据库中只存储id questionsJSON statisticalAnswersJSON这3个属性
 * 也就是说问卷的问题数据和统计回答数据都是以JSON数据的形式存到数据库的
 */
@Getter@ToString
public class Questionnaire {
    /**
     * 把数据存到数据库后产生的唯一标识
     */
    @Setter
    private Integer id;

    /**
     * 该问卷的所有问题
     */
    private List<Question> questions;

    /**
     * questions的JSON数据
     */
    private String questionsJSON;

    /**
     * 该文件的所有问题对应的经过统计的答案
     */
    private List<StatisticalAnswer> statisticalAnswers;

    /**
     * statisticalAnswers的JSON数据
     */
    private String statisticalAnswersJSON;

    /*
     * 自定义Setter方法
     */
    public void setQuestions(List<Question> questions){
        this.questions = questions;
        flushQuestionsJSON();
    }
    public void setQuestionsJSON(String questionsJSON){
        this.questionsJSON = questionsJSON;
        flushQuestions();
    }
    public void setStatisticalAnswers(List<StatisticalAnswer> statisticalAnswers){
        this.statisticalAnswers = statisticalAnswers;
        flushStatisticalAnswersJSON();
    }
    public void setStatisticalAnswersJSON(String statisticalAnswersJSON){
        this.statisticalAnswersJSON = statisticalAnswersJSON;
        flushStatisticalAnswers();
    }

    public void flushQuestionsJSON(){
        if(this.questions == null){
            this.questionsJSON = null;
            return;
        }
        try {
            this.questionsJSON = new ObjectMapper().writeValueAsString(questions);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    public void flushQuestions(){
        if(this.questionsJSON == null){
            this.questions = null;
            return;
        }
        try {
            this.questions = new ObjectMapper().readValue(questionsJSON, new TypeReference<List<Question>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void flushStatisticalAnswersJSON(){
        if(this.statisticalAnswers == null){
            this.statisticalAnswersJSON =null;
            return;
        }
        try {
            this.statisticalAnswersJSON = new ObjectMapper().writeValueAsString(statisticalAnswers);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    public void flushStatisticalAnswers(){
        if(this.statisticalAnswersJSON == null){
            this.statisticalAnswers = null;
            return;
        }
        try {
            this.statisticalAnswers = new ObjectMapper().readValue(statisticalAnswersJSON, new TypeReference<List<StatisticalAnswer>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
