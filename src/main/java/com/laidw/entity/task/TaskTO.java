package com.laidw.entity.task;

import com.laidw.entity.questionnaire.Questionnaire;
import com.laidw.entity.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 使用了装饰者模式，主要是用来表示用户接收的任务
 * 用户接收的任务不是可以直接用Task类来表示吗？
 * 非也！用户接收的任务肯定要有一个isDone属性来表示用户是否已经完成了这个任务
 * 但是isDone属性不能直接定义在Task类中（这种做法对NormalTask没问题，但对InvestigatingTask有问题）
 * 一个InvestigatingTask可以被多个用户接收，isDone属性定义在InvestigatingTask类中会使得所有用户共享这个属性
 * 从而导致：一个用户完成该任务后，所有接收该任务的用户都“完成了”该任务
 * 因此，用户和他接收的任务之间的对应关系应该存于一个新表user_acc_task中，该表还应该包含is_done字段
 * 用户在查询他接收的任务时，需要通过查user_acc_task表找到他接收的任务，同时还应该把每个任务对应的isDone属性告知用户
 * 因此就产生了这个类，用于封装任务的数据和及其对应的isDone属性
 * 另外要注意，本类底层使用的对象是Task对象，NormalTask类和Task类是一样的，而InvestigatingTask则还要多一些属性
 * 因此在知道了底层的对象是InvestigatingTask对象后，如果要获得该对象的特有属性，只需要进行强转即可
 */
@Getter@Setter
public class TaskTO extends Task {
    private Task task;
    private Boolean isDone;

    public TaskTO(){
        task = new Task();
    }

    public void setType(Type type) {
        task.setType(type);
    }
    public void setTitle(String title) {task.setTitle(title);}
    public void setPayment(Double payment) {
        task.setPayment(payment);
    }
    public void setDescription(String description) {
        task.setDescription(description);
    }
    public void setAcceptors(List<User> acceptors) {
        task.setAcceptors(acceptors);
    }
    public void setPublisher(User publisher) {
        task.setPublisher(publisher);
    }
    public void setId(Integer id) {
        task.setId(id);
    }
    public void setIsAvailable(Boolean isAvailable) {task.setIsAvailable(isAvailable);}
    public void setCurCount(Integer curCount) {task.setCurCount(curCount);}
    public void setMaxCount(Integer maxCount) {task.setMaxCount(maxCount);}

    public void setGradeLimit(String gradeLimit) {((InvestigatingTask)task).setGradeLimit(gradeLimit);}
    public void setGenderLimit(Integer genderLimit) {((InvestigatingTask)task).setGenderLimit(genderLimit);}
    public void setAgeLimit(String ageLimit) {((InvestigatingTask)task).setAgeLimit(ageLimit);}
    public void setMajorLimit(String majorLimit) {((InvestigatingTask)task).setMajorLimit(majorLimit);}
    public void setQuestionnaire(Questionnaire questionnaire){((InvestigatingTask)task).setQuestionnaire(questionnaire);}


    public Type getType() {
        return task.getType();
    }
    public String getTitle() {return task.getTitle();}
    public Double getPayment() {
        return task.getPayment();
    }
    public String getDescription() {
        return task.getDescription();
    }
    public List<User> getAcceptors() {
        return task.getAcceptors();
    }
    public User getPublisher() {
        return task.getPublisher();
    }
    public Integer getId() {
        return task.getId();
    }
    public Boolean getIsAvailable() {return task.getIsAvailable();}
    public Integer getCurCount() {return task.getCurCount();}
    public Integer getMaxCount() {return task.getMaxCount();}

    public String getGradeLimit(){return ((InvestigatingTask)task).getGradeLimit();}
    public Integer getGenderLimit(){return ((InvestigatingTask)task).getGenderLimit();}
    public String getAgeLimit(){return ((InvestigatingTask)task).getAgeLimit();}
    public String getMajorLimit(){return ((InvestigatingTask)task).getMajorLimit();}
    public Questionnaire getQuestionnaire(){return ((InvestigatingTask)task).getQuestionnaire();}

    /**
     * 重写toString()方法
     * @return 转换成的字符串
     */
    public String toString() {
        return task.toString() + " isDone : " + this.isDone;
    }
}
