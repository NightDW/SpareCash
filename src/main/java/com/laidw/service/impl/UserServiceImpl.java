package com.laidw.service.impl;

import com.laidw.entity.user.Cow;
import com.laidw.entity.user.Student;
import com.laidw.entity.user.User;
import com.laidw.mapper.UserMapper;
import com.laidw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @SuppressWarnings({"SpringJavaAutowiredFieldsWarningInspection", "SpringJavaInjectionPointsAutowiringInspection"})
    @Autowired private UserMapper mapper;

    public void insertStudent(Student student) {
        mapper.insertStudent(student);
    }

    public void insertCow(Cow cow) {
        mapper.insertCow(cow);
    }

    public void deleteUserById(Integer id) {
        mapper.deleteUserById(id);
    }

    public void updateStudentById(Student stu) {
        mapper.updateStudentById(stu);
    }

    public void updateStudentByIdSelectively(Student stu) {
        mapper.updateStudentByIdSelectively(stu);
    }

    public void updateCowById(Cow cow) {
        mapper.updateCowById(cow);
    }

    public void updateCowByIdSelectively(Cow cow) {
        mapper.updateCowByIdSelectively(cow);
    }

    public void setUserIsActive(Integer uid, Boolean isActive) {
        mapper.setUserIsActive(uid, isActive);
    }

    public Boolean checkIfExistExceptMe(String uid, String name, String phone, String email, String myId) {
        User user = mapper.selectUserIfExistExceptMe(uid, name, phone, email, myId);
        return user != null;
    }

    public User selectUserById(Integer id) {
        return mapper.selectUserById(id);
    }

    public User selectUserWhoHas(String uid, String name, String phone, String email) {
        return mapper.selectUserWhoHas(uid, name, phone, email);
    }

    public List<User> selectAllUsers() {
        return mapper.selectAllUsers();
    }

    public List<User> selectAllActiveUsers() {
        return mapper.selectAllActiveUsers();
    }
}
