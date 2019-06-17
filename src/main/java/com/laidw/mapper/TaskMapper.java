package com.laidw.mapper;

import com.laidw.entity.task.InvestigatingTask;
import com.laidw.entity.task.NormalTask;
import com.laidw.entity.task.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TaskMapper {
    void insertNormalTask(NormalTask task);
    void insertInvestigatingTask(InvestigatingTask task);

    void deleteTaskById(Integer id);

    void updateNormalTaskById(NormalTask task);
    void updateNormalTaskByIdSelectively(NormalTask task);
    void updateInvestigatingTaskById(InvestigatingTask task);
    void updateInvestigatingTaskByIdSelectively(InvestigatingTask task);
    void updateTaskCurCount(@Param("tid") Integer tid, @Param("step") Integer step);

    void setTaskAvail(@Param("tid") Integer tid, @Param("isAvailable") Boolean isAvailable);

    Task selectTaskById(Integer id);
    Task selectInvTaskByQueId(Integer qid);
    List<Task> selectAllTasks();
    List<Task> selectAllAvailableTasks();
    List<Task> selectAllMyPublishedTasks(Integer myId);
}
