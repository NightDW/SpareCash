package com.laidw.service;

import com.laidw.entity.pagebean.PageBean;
import com.laidw.entity.task.InvestigatingTask;
import com.laidw.entity.task.NormalTask;
import com.laidw.entity.task.Task;

import java.util.List;

public interface TaskService {
    void insertNormalTask(NormalTask task);
    void insertInvestigatingTask(InvestigatingTask task);

    void deleteTaskById(Integer id);

    void updateNormalTaskById(NormalTask task);
    void updateNormalTaskByIdSelectively(NormalTask task);
    void updateInvestigatingTaskById(InvestigatingTask task);
    void updateInvestigatingTaskByIdSelectively(InvestigatingTask task);

    void setTaskAvail(Integer tid, Boolean isAvailable);

    Task selectTaskById(Integer id);
    Task selectInvTaskByQueId(Integer qid);
    PageBean<Task> selectAllTasksLimits(Integer pageNum, Integer pageSize, Integer navigatePages);
    PageBean<Task> selectAllAvailableTasksLimits(Integer pageNum, Integer pageSize, Integer navigatePages);
    PageBean<Task> selectAllMyPublishedTasksLimits(Integer pageNum, Integer pageSize, Integer navigatePages, Integer myId);
}
