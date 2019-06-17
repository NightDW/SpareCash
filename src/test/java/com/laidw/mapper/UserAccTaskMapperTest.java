package com.laidw.mapper;

import com.laidw.entity.task.TaskTO;
import com.laidw.entity.user.UserTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 测试顺序：4
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserAccTaskMapperTest {
    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private UserAccTaskMapper mapper;

    @Test
    public void test1(){
        mapper.setUserAcceptTask(1,1);
        mapper.setUserAcceptTask(1,2);
        mapper.setUserAcceptTask(1,4);
        mapper.setUserAcceptTask(2,2);
        mapper.setUserAcceptTask(2,3);
        mapper.setUserAcceptTask(2,4);
    }

    @Test
    public void test2(){
        mapper.setUserFinishTask(1,1);
        mapper.setUserCancelTask(2,4);
    }

    @Test
    public void test3(){
        List<TaskTO> list1 = mapper.findUserAcceptedTasks(1);
        for(TaskTO t : list1)
            System.out.println(t);
        List<UserTO> list2 = mapper.findUsersWhoAcceptedThisTask(2);
        for(UserTO u : list2)
            System.out.println(u);
    }
}
