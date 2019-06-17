package com.laidw.entity.task;

import com.laidw.entity.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 本类用于存储任务的基本数据；主要是作为父类来给子类继承的
 * 任务分为普通任务（如A让B取快递）和调查任务（如A写一个调查问卷让B C D填）
 * 学生可以发布普通任务，接收普通任务，接收调查任务
 * 奶牛可以发布普通任务，发布调查任务
 * 普通任务只能被一个用户接收，而调查任务可以被多个用户接收
 */
@Getter@Setter
public class Task {
    /**
     * 把数据存到数据库后产生的唯一标识
     */
    private Integer id;

    /**
     * 该任务的发布者
     */
    private User publisher;

    /**
     * 该任务的接收者
     * 注意这里没有区分普通任务和调查任务，即不能确定接收者是一个还是多个，所以统一用List&lt;User&gt;集合来表示该任务的接收者
     * 同时由于没有区分普通任务和调查任务，所以被接收的任务和用户之间是多对多的关系，需要建一个新表user_acc_task
     */
    private List<User> acceptors;

    /**
     * 该任务的标题，用于表示该任务的简略信息，如“帮帮忙！”等
     */
    private String title;

    /**
     * 该任务的描述，比如"请帮我到X地方取快递，送到Y地方的Z人物手里"等
     */
    private String description;

    /**
     * 该任务的报酬
     */
    private Double payment;

    /**
     * 该任务的类型，即普通任务或调查任务
     */
    private Type type;

    /*
     * 该任务是否已可被用户接收，即是否尚未结束；用户在搜索可接收的任务时将会用到这个属性
     * 注意，在搜索可接收的调查任务时，只要该任务isAvailable且该任务未满员，就把它展示给用户，不管用户是否满足问卷指定的条件
     * 在用户决定接收某个调查任务时，才判断该用户是否满足条件
     * 这是因为，要只找符合该用户信息的调查任务实现起来比较复杂且费时
     * 比如用户21岁，读大一，那么在遍历每个调查任务时都要确定该任务是否可以被21岁的大一学生接收，会浪费很多资源
     * 不过，作为弥补，在显示调查任务时应该把该任务的要求显示出来，让用户自己判断其是否有资格获取该任务
     */
    private Boolean isAvailable;

    /**
     * 接收了该任务的人数
     */
    private Integer curCount;

    /**
     * 该任务允许被多少人接收
     * 对于普通任务，该固定值为1
     * 对于调查任务，该值由发布者决定，用于确定要调查的人数
     * 当curCount < maxCount时，该任务是可以被接收的，否则不能接收
     */
    private Integer maxCount;

    /**
     * 重写toString()方法，注意本类和User类是相互包含的关系，为了防止来回调用的死循环
     * 把本类对象转成字符串时，不能直接把该对象中的User对象toString，而只显示该User对象的id
     * @return 转换成的字符串
     */
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", publisher=" + publisher.getId() +
                ", acceptors=" + acceptorsToString() +
                ", description='" + description + '\'' +
                ", payment=" + payment +
                ", type=" + type +
                //", isAvailable=" + isAvailable +
                ", curCount=" + curCount +
                ", maxCount=" + maxCount +
                '}';
    }

    private String acceptorsToString(){
        StringBuilder ret = new StringBuilder("[");
        for(User user : this.acceptors)
            ret.append(user.getId()).append(",");
        if(ret.lastIndexOf(",") != -1)
            ret.deleteCharAt(ret.lastIndexOf(","));
        ret.append("]");
        return ret.toString();
    }
}
