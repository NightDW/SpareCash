package com.laidw.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.laidw.entity.pagebean.PageBean;
import com.laidw.entity.task.InvestigatingTask;
import com.laidw.entity.task.NormalTask;
import com.laidw.entity.task.Task;
import com.laidw.mapper.TaskMapper;
import com.laidw.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    @SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection", "SpringJavaAutowiredFieldsWarningInspection"})
    @Autowired private TaskMapper mapper;

    public void insertNormalTask(NormalTask task) {
        mapper.insertNormalTask(task);
    }

    public void insertInvestigatingTask(InvestigatingTask task) {
        mapper.insertInvestigatingTask(task);
    }

    public void deleteTaskById(Integer id) {
        mapper.deleteTaskById(id);
    }

    public void updateNormalTaskById(NormalTask task) {
        mapper.updateNormalTaskById(task);
    }

    public void updateNormalTaskByIdSelectively(NormalTask task) {
        mapper.updateNormalTaskByIdSelectively(task);
    }

    public void updateInvestigatingTaskById(InvestigatingTask task) {
        mapper.updateInvestigatingTaskById(task);
    }

    public void updateInvestigatingTaskByIdSelectively(InvestigatingTask task) {
        mapper.updateInvestigatingTaskByIdSelectively(task);
    }

    public void setTaskAvail(Integer tid, Boolean isAvailable) {
        mapper.setTaskAvail(tid, isAvailable);
    }

    public Task selectTaskById(Integer id) {
        return mapper.selectTaskById(id);
    }

    public Task selectInvTaskByQueId(Integer qid) {
        return mapper.selectInvTaskByQueId(qid);
    }

    public PageBean<Task> selectAllTasksLimits(Integer pageNum, Integer pageSize, Integer navigatePages) {
        Page<Task> page = PageHelper.startPage(pageNum, pageSize);
        List<Task> list = mapper.selectAllTasks();
        PageInfo<Task> pageInfo = new PageInfo<>(list, navigatePages);
        return new PageBean<>(list, page, pageInfo);
    }

    public PageBean<Task> selectAllAvailableTasksLimits(Integer pageNum, Integer pageSize, Integer navigatePages) {
        Page<Task> page = PageHelper.startPage(pageNum, pageSize);
        List<Task> list = mapper.selectAllAvailableTasks();
        PageInfo<Task> pageInfo = new PageInfo<>(list, navigatePages);
        return new PageBean<>(list, page, pageInfo);
    }

    public PageBean<Task> selectAllMyPublishedTasksLimits(Integer pageNum, Integer pageSize, Integer navigatePages, Integer myId) {
        Page<Task> page = PageHelper.startPage(pageNum, pageSize);
        List<Task> list = mapper.selectAllMyPublishedTasks(myId);
        PageInfo<Task> pageInfo = new PageInfo<>(list, navigatePages);
        return new PageBean<>(list, page, pageInfo);
    }
}
