package com.laidw.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.laidw.entity.pagebean.PageBean;
import com.laidw.entity.task.InvestigatingTask;
import com.laidw.entity.task.Task;
import com.laidw.entity.task.TaskTO;
import com.laidw.entity.task.Type;
import com.laidw.entity.user.Identity;
import com.laidw.entity.user.Student;
import com.laidw.entity.user.User;
import com.laidw.entity.user.UserTO;
import com.laidw.mapper.TaskMapper;
import com.laidw.mapper.UserAccTaskMapper;
import com.laidw.mapper.UserMapper;
import com.laidw.service.UserAccTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class UserAccTaskServiceImpl implements UserAccTaskService {

    @SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection", "SpringJavaAutowiredFieldsWarningInspection"})
    @Autowired private UserAccTaskMapper mapper;

    @SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection", "SpringJavaAutowiredFieldsWarningInspection"})
    @Autowired private TaskMapper taskMapper;

    @SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection", "SpringJavaAutowiredFieldsWarningInspection"})
    @Autowired private UserMapper userMapper;

    public void userAcceptTask(Integer uid, Integer tid) throws Exception {
        User user = userMapper.selectUserById(uid);
        if(user.getIdentity().equals(Identity.COW))
            throw new Exception("奶牛无法接收任务！");

        if(mapper.isDone(uid, tid) != null)
            throw new Exception("用户已接收了该任务！");

        Task task = taskMapper.selectTaskById(tid);
        if(!task.getIsAvailable() || task.getCurCount() == task.getMaxCount().intValue())
            throw new Exception("该任务已结束或接收的人数已满！");

        if(task.getType().equals(Type.INVESTIGATING)){
            Student stu =  (Student) user;
            InvestigatingTask iTask = (InvestigatingTask) task;

            if(iTask.getGradeLimit() != null){
                String[] gradeLimitSplit = iTask.getGradeLimit().split("-");
                Integer grade = stu.getGrade();
                if(grade < Integer.parseInt(gradeLimitSplit[0]) || grade > Integer.parseInt(gradeLimitSplit[1]))
                    throw new Exception("年级不符合要求！");
            }

            if(iTask.getGenderLimit() != null){
                if(iTask.getGenderLimit() != stu.getGender().intValue())
                    throw new Exception("性别不符合要求！");
            }

            if(iTask.getAgeLimit() != null){
                String[] ageLimitSplit = iTask.getAgeLimit().split("-");
                Integer age = stu.getAge();
                if(age < Integer.parseInt(ageLimitSplit[0]) || age > Integer.parseInt(ageLimitSplit[1]))
                    throw new Exception("年龄不符合要求！");
            }

            if(iTask.getMajorLimit() != null){
                String[] majorLimitSplit = iTask.getMajorLimit().split(",");
                String major = stu.getMajor();
                if(!Arrays.asList(majorLimitSplit).contains(major))
                    throw new Exception("专业不符合要求！");
            }
        }

        mapper.setUserAcceptTask(uid, tid);
        //需要把当前任务的curCount加1
        taskMapper.updateTaskCurCount(tid, 1);
    }

    public void userFinishTask(Integer uid, Integer tid) {
        if(!mapper.isDone(uid, tid)){
            Task task = taskMapper.selectTaskById(tid);
            mapper.setUserFinishTask(uid, tid);
            userMapper.updateSpareCashCoin(task.getPublisher().getId(), -(task.getPayment()));
            userMapper.updateSpareCashCoin(uid, task.getPayment());
        }
    }

    public void userCancelTask(Integer uid, Integer tid) {
        mapper.setUserCancelTask(uid, tid);
        //需要把当前任务的curCount减一
        taskMapper.updateTaskCurCount(tid, -1);
    }

    public Boolean isDone(Integer uid, Integer tid) {
        return mapper.isDone(uid, tid);
    }

    public PageBean<TaskTO> findUserAcceptedTasksLimits(Integer pageNum, Integer pageSize, Integer navigatePages, Integer uid) {
        Page<TaskTO> page = PageHelper.startPage(pageNum, pageSize);
        List<TaskTO> list = mapper.findUserAcceptedTasks(uid);
        PageInfo<TaskTO> pageInfo = new PageInfo<>(list, navigatePages);
        return new PageBean<>(list, page, pageInfo);
    }

    public PageBean<UserTO> findUsersWhoAcceptedThisTaskLimits(Integer pageNum, Integer pageSize, Integer navigatePages, Integer tid) {
        Page<UserTO> page = PageHelper.startPage(pageNum, pageSize);
        List<UserTO> list = mapper.findUsersWhoAcceptedThisTask(tid);
        PageInfo<UserTO> pageInfo = new PageInfo<>(list, navigatePages);
        return new PageBean<>(list, page, pageInfo);
    }
}
