package com.laidw.mapper;

import com.laidw.entity.questionnaire.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 测试顺序：1
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class QuestionnaireMapperTest {
    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private QuestionnaireMapper mapper;

    private Questionnaire questionnaire;
    private List<Question> questions;
    private List<StatisticalAnswer> statisticalAnswers;

    @Test
    public void testInsert(){
        for(int i = 0; i < 9; i++)
            mapper.insertQuestionnaire(questionnaire);
    }

    @Test
    public void testSelect(){
        Questionnaire ques = mapper.selectQuestionnaireById(1);
        for(Question q : ques.getQuestions())
            System.out.println(q);
        for(StatisticalAnswer s : ques.getStatisticalAnswers())
            System.out.println(s);
        List<Questionnaire> list = mapper.selectAllQuestionnaires();
        for(Questionnaire q : list)
            System.out.println(q);
    }

    @Test
    public void testDelete(){
        mapper.deleteQuestionnaireById(9);
    }

    @Test
    public void testUpdate(){
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setId(7);
        questionnaire.setQuestions(Arrays.asList(null, null));
        mapper.updateQuestionnaireByIdSelectively(questionnaire);

        questionnaire.setId(8);
        questionnaire.setStatisticalAnswers(Arrays.asList(null, null));
        mapper.updateQuestionnaireById(questionnaire);
    }

    @Before
    public void initQuestionnaire(){
        initQuestions(); initStatisticalAnswers();
        questionnaire = new Questionnaire();
        questionnaire.setQuestions(questions);
        questionnaire.setStatisticalAnswers(statisticalAnswers);
    }

    private void initQuestions(){
        questions = new ArrayList<>(); Question q;
        //该问卷有3个问题，分别为单选题，多选题和问答题
        q = new Question();
        q.setNum(1);
        q.setTitle("Your Name?");
        q.setOptions(Arrays.asList("zs", "ls", "ww"));
        q.setType(Type.SINGLE);
        q.setIsRequired(true);
        questions.add(q);
        q = new Question();
        q.setNum(2);
        q.setTitle("Your Hobbies?");
        q.setOptions(Arrays.asList("cy", "hj", "tt"));
        q.setType(Type.MULTI);
        q.setIsRequired(false);
        questions.add(q);
        q = new Question();
        q.setNum(3);
        q.setTitle("Your Suggest?");
        q.setOptions(null);
        q.setType(Type.ASKING);
        q.setIsRequired(false);
        questions.add(q);
    }
    private void initStatisticalAnswers(){
        statisticalAnswers = new ArrayList<>();
        StatisticalAnswer sa; Answer a1, a2;
        //第一个人选了第一个选项，第二个人选了第二个选项
        sa = new StatisticalAnswer(1, Type.SINGLE);
        a1 = new Answer(); a1.setNum(1); a1.setType(Type.SINGLE); a1.setAnswer("128");
        a2 = new Answer(); a2.setNum(1); a2.setType(Type.SINGLE); a2.setAnswer("64");
        statisticalAnswers.add(sa.addAnswer(a1).addAnswer(a2));
        //第一个人选12选项，第二个人选23选项
        sa = new StatisticalAnswer(2, Type.MULTI);
        a1 = new Answer(); a1.setNum(2); a1.setType(Type.MULTI); a1.setAnswer("192");
        a2 = new Answer(); a2.setNum(2); a2.setType(Type.MULTI); a2.setAnswer("96");
        statisticalAnswers.add(sa.addAnswer(a1).addAnswer(a2));
        //第一个人回答"No Suggest!"，第二个人回答"Me Too!"
        sa = new StatisticalAnswer(3, Type.ASKING);
        a1 = new Answer(); a1.setNum(3); a1.setType(Type.ASKING); a1.setAnswer("No Suggest!");
        a2 = new Answer(); a2.setNum(3); a2.setType(Type.ASKING); a2.setAnswer("Me Too!");
        statisticalAnswers.add(sa.addAnswer(a1).addAnswer(a2));
    }
}
