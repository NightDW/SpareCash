package com.laidw.mapper;

import com.laidw.entity.questionnaire.Questionnaire;
import com.laidw.entity.task.InvestigatingTask;
import com.laidw.entity.task.NormalTask;
import com.laidw.entity.task.Task;
import com.laidw.entity.task.Type;
import com.laidw.entity.user.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 测试顺序：3
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TaskMapperTest {
    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private TaskMapper mapper;

    private NormalTask n;
    private InvestigatingTask i;

    @Test
    public void testInsert(){
        for(int a = 0; a < 3; a++){
            mapper.insertNormalTask(n);
            mapper.insertInvestigatingTask(i);
        }
    }

    @Test
    public void testSelect(){
        System.out.println(mapper.selectTaskById(1));
        List<Task> list;
        list = mapper.selectAllTasks();
        for(Task t : list )
            System.out.println(t);
        list = mapper.selectAllAvailableTasks();
        for(Task t : list )
            System.out.println(t);
        list = mapper.selectAllMyPublishedTasks(1);
        for(Task t : list )
            System.out.println(t);
    }

    @Test
    public void testUpdate(){
        NormalTask nt = new NormalTask();
        nt.setId(3);
        //nt.setIsAvailable(false);
        mapper.updateNormalTaskByIdSelectively(nt);
        User user = new User();
        user.setId(2);
        nt.setId(5);
        nt.setPublisher(user);
        nt.setDescription("NewNor");
        nt.setPayment(2d);
        nt.setType(Type.NORMAL);
        //nt.setIsAvailable(false);
        mapper.updateNormalTaskById(nt);

        InvestigatingTask it = new InvestigatingTask();
        it.setId(4);
        it.setAgeLimit("33-44");
        mapper.updateInvestigatingTaskByIdSelectively(it);
        user.setId(3);
        it.setId(6);
        it.setPublisher(user);
        it.setDescription("NewInv");
        it.setPayment(2d);
        it.setType(Type.INVESTIGATING);
        //it.setIsAvailable(false);
        mapper.updateInvestigatingTaskById(it);
    }

    @Test
    public void testDelete(){
        mapper.deleteTaskById(5);
        mapper.deleteTaskById(6);
    }

    @Before
    public void initData(){
        User user1 = new User(); user1.setId(1);
        n = new NormalTask();
        n.setPublisher(user1);
        n.setDescription("Normal");
        n.setPayment(1d);
        n.setType(Type.NORMAL);
        //n.setIsAvailable(true);
        n.setAcceptors(null);

        User user3 = new User(); user3.setId(3);
        Questionnaire q = new Questionnaire(); q.setId(1);
        i = new InvestigatingTask();
        i.setPublisher(user3);
        i.setDescription("Investigating");
        i.setPayment(1d);
        i.setType(Type.INVESTIGATING);
        //i.setIsAvailable(true);
        i.setAcceptors(null);
        i.setGradeLimit("1-2");
        i.setGenderLimit(0);
        i.setAgeLimit("22-23");
        i.setMajorLimit(null);
        i.setQuestionnaire(q);
    }
}
