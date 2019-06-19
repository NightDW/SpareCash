package com.laidw.service.impl;

import com.laidw.entity.questionnaire.Questionnaire;
import com.laidw.mapper.QuestionnaireMapper;
import com.laidw.service.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class QuestionnaireServiceImpl implements QuestionnaireService {

    @SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection", "SpringJavaAutowiredFieldsWarningInspection"})
    @Autowired private QuestionnaireMapper mapper;

    public void insertQuestionnaire(Questionnaire questionnaire) {
        mapper.insertQuestionnaire(questionnaire);
    }

    public void deleteQuestionnaireById(Integer id) {
        mapper.deleteQuestionnaireById(id);
    }

    public void updateQuestionnaireById(Questionnaire questionnaire) {
        mapper.updateQuestionnaireById(questionnaire);
    }

    public void updateQuestionnaireByIdSelectively(Questionnaire questionnaire) {
        mapper.updateQuestionnaireByIdSelectively(questionnaire);
    }

    public Questionnaire selectQuestionnaireById(Integer id) {
        return mapper.selectQuestionnaireById(id);
    }

    public List<Questionnaire> selectAllQuestionnaires() {
        return mapper.selectAllQuestionnaires();
    }
}
