package com.laidw.service;

import com.laidw.entity.questionnaire.Questionnaire;

import java.util.List;

public interface QuestionnaireService {
    void insertQuestionnaire(Questionnaire questionnaire);

    void deleteQuestionnaireById(Integer id);

    void updateQuestionnaireById(Questionnaire questionnaire);
    void updateQuestionnaireByIdSelectively(Questionnaire questionnaire);

    Questionnaire selectQuestionnaireById(Integer id);
    List<Questionnaire> selectAllQuestionnaires();
}
