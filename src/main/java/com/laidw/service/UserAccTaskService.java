package com.laidw.service;

import com.laidw.entity.pagebean.PageBean;
import com.laidw.entity.task.TaskTO;
import com.laidw.entity.user.UserTO;

import java.util.List;

public interface UserAccTaskService {
    void userAcceptTask(Integer uid, Integer tid) throws Exception;

    //用户提交了问卷的答案后就认为用户完成了调查任务，也就是说填了问卷后就开始转账
    //普通任务则需要发布者确认，只有发布者确认完成了才算完成了，此时再进行转账
    //换句话说，如果tid对应的任务是普通任务，则必须在确认当前登录用户是发布者后才调用该方法
    void userFinishTask(Integer uid, Integer tid);

    void userCancelTask(Integer uid, Integer tid);

    Boolean isDone(Integer uid, Integer tid);
    PageBean<TaskTO> findUserAcceptedTasksLimits(Integer pageNum, Integer pageSize, Integer navigatePages, Integer uid);
    PageBean<UserTO> findUsersWhoAcceptedThisTaskLimits(Integer pageNum, Integer pageSize, Integer navigatePages, Integer tid);
}
