package com.laidw.mapper;

import com.laidw.entity.questionnaire.Questionnaire;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QuestionnaireMapper {
    void insertQuestionnaire(Questionnaire questionnaire);

    void deleteQuestionnaireById(Integer id);

    void updateQuestionnaireById(Questionnaire questionnaire);
    void updateQuestionnaireByIdSelectively(Questionnaire questionnaire);

    Questionnaire selectQuestionnaireById(Integer id);
    List<Questionnaire> selectAllQuestionnaires();
}
