package com.laidw.entity.user;

import com.laidw.entity.task.Task;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 本类用于存储普通用户，也就是学生的数据
 */
@Getter@Setter
public class Student extends User {
    /**
     * 该学生目前读大几，0表示其它，1代表大一，2代表大二等
     */
    private Integer grade;

    /**
     * 该学生的性别，0代表女，1代表男
     */
    private Integer gender;

    /**
     * 该学生的年龄
     */
    private Integer age;

    /**
     * 该学生的专业
     */
    private String major;

    /**
     * 该学生接收的任务
     * 注意这里没有区分普通任务和调查任务
     */
    private List<Task> acceptedTasks;

    /**
     * 重写toString()方法，接收的任务同样只取其id
     * @return 转换成的字符串
     */
    @Override
    public String toString() {
        return "Student" + super.toString() + "{" +
                "grade=" + grade +
                ", gender=" + gender +
                ", age=" + age +
                ", major='" + major + '\'' +
                ", acceptedTasks=" + acceptedTasksToString() +
                '}';
    }

    private String acceptedTasksToString(){
        StringBuilder ret = new StringBuilder("[");
        for(Task task : this.acceptedTasks)
            ret.append(task.getId()).append(",");
        if(ret.lastIndexOf(",") != -1)
            ret.deleteCharAt(ret.lastIndexOf(","));
        ret.append("]");
        return ret.toString();
    }
}
