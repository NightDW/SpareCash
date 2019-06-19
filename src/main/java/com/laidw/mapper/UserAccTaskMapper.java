package com.laidw.mapper;

import com.laidw.entity.task.TaskTO;
import com.laidw.entity.user.UserTO;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserAccTaskMapper {
    //注意，如果用户接收的是普通任务，那么除了调用这个方法，还需要把该任务的isAvailable属性设为false
    //更新：任何任务被接收时，除了调用这个方法，还需要将其curCount加1
    void setUserAcceptTask(@Param("uid") Integer uid, @Param("tid") Integer tid);

    void setUserFinishTask(@Param("uid") Integer uid, @Param("tid") Integer tid);

    //注意，如果用户取消的是普通任务，那么除了调用这个方法，还需要把该任务的isAvailable属性设为true
    //更新：任何任务被取消时，除了调用这个方法，还需要将其curCount减1
    void setUserCancelTask(@Param("uid") Integer uid, @Param("tid") Integer tid);

    Boolean isDone(@Param("uid") Integer uid, @Param("tid") Integer tid);
    List<TaskTO> findUserAcceptedTasks(Integer uid);
    List<UserTO> findUsersWhoAcceptedThisTask(Integer tid);
}
