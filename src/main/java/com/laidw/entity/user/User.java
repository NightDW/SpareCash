package com.laidw.entity.user;

import com.laidw.entity.task.Task;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * 本类用于存储用户的基本数据；主要是作为父类来给子类继承的
 * 用户可以是普通用户（即学生），也可以是机构（即奶牛）
 * 学生可以发布普通任务，接收普通任务，接收调查任务
 * 奶牛可以发布普通任务，发布调查任务
 * 注意本类实现了UserDetails接口，SpringSecurity可以直接从本类对象中获取该用户的数据
 */
@Getter@Setter
public class User implements UserDetails {
    /**
     * 把数据存到数据库后产生的唯一标识
     */
    private Integer id;

    /**
     * 学生的学号或机构的机构号
     */
    private Integer uid;

    /**
     * 学生或机构的名称
     */
    private String name;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 该用户的头像
     */
    private String iconUrl;

    /**
     * 该用户的电话
     */
    private String phone;

    /**
     * 该用户的邮箱
     */
    private String email;

    /**
     * 该用户拥有的闲钱币
     */
    private Double spareCashCoin;

    /**
     * 该用户发布的任务
     * 注意这里没有区分普通任务和调查任务
     * 如果不注意可能会出现学生发布调查任务的情况
     */
    private List<Task> publishedTasks;

    /**
     * 该用户是否已激活
     */
    private Boolean isActive;

    /**
     * 该用户的身份，即学生或奶牛
     */
    private Identity identity;

    /**
     * 该用户的验证码，用户在注册时，提交注册的数据后由Controller生成验证码并把数据保存到数据库中
     * 然后系统给用户注册的邮箱发送验证链接，用户点击这个链接后进入校验阶段，校验成功则激活该账户
     */
    private String verifyCode;

    /**
     * 重写toString()方法，注意只显示接收的任务的id
     * @return 转换成的字符串
     */
    public String toString() {
        return "User{" +
                "id=" + id +
                ", uid=" + uid +
                ", name='" + name + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", spareCashCoin=" + spareCashCoin +
                ", publishedTasks=" + publishedTasksToString() +
                ", isActive=" + isActive +
                ", identity=" + identity +
                '}';
    }

    private String publishedTasksToString(){
        StringBuilder ret = new StringBuilder("[");
        for(Task task : this.publishedTasks)
            ret.append(task.getId()).append(",");
        if(ret.lastIndexOf(",") != -1)
            ret.deleteCharAt(ret.lastIndexOf(","));
        ret.append("]");
        return ret.toString();
    }

    /**
     * 获取用户的权限信息，本工程不涉及复杂的权限，直接返回该用户的角色（即身份）即可
     * @return 封装后的用户的身份信息
     */
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.identity.name().toLowerCase()));
        return authorities;
    }

    public String getUsername() {
        return this.name;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return this.isActive;
    }
}
