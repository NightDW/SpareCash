package com.laidw.entity.user;

import com.laidw.entity.task.Task;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 本类和TaskTO类的作用类似
 */
@Getter@Setter
public class UserTO extends User {
    private User user;
    
    public UserTO(){
        user = new User();
    }

    public String toString() {
        return user.toString();
    }
    
    public Integer getId() {
        return user.getId();
    }
    public Integer getUid() {
        return user.getUid();
    }
    public String getName() {
        return user.getName();
    }
    public String getIconUrl() {
        return user.getIconUrl();
    }
    public String getPhone() {
        return user.getPhone();
    }
    public String getEmail() {
        return user.getEmail();
    }
    public List<Task> getPublishedTasks() {
        return user.getPublishedTasks();
    }
    public Double getSpareCashCoin() {
        return user.getSpareCashCoin();
    }
    public Boolean getIsActive() {
        return user.getIsActive();
    }
    public Identity getIdentity() {
        return user.getIdentity();
    }

    public void setId(Integer id) {
        user.setId(id);
    }
    public void setUid(Integer uid) {
        user.setUid(uid);
    }
    public void setName(String name) {
        user.setName(name);
    }
    public void setIconUrl(String iconUrl) {
        user.setIconUrl(iconUrl);
    }
    public void setPhone(String phone) {
        user.setPhone(phone);
    }
    public void setEmail(String email) {
        user.setEmail(email);
    }
    public void setSpareCashCoin(Double spareCashCoin) {
        user.setSpareCashCoin(spareCashCoin);
    }
    public void setPublishedTasks(List<Task> publishedTasks) {
        user.setPublishedTasks(publishedTasks);
    }
    public void setIsActive(Boolean isActive) {
        user.setIsActive(isActive);
    }
    public void setIdentity(Identity identity) {
        user.setIdentity(identity);
    }
}
