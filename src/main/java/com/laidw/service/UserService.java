package com.laidw.service;

import com.laidw.entity.user.Cow;
import com.laidw.entity.user.Student;
import com.laidw.entity.user.User;

import java.util.List;

public interface UserService {
    void insertStudent(Student student);
    void insertCow(Cow cow);

    void deleteUserById(Integer id);

    void updateStudentById(Student stu);
    void updateStudentByIdSelectively(Student stu);
    void updateCowById(Cow cow);
    void updateCowByIdSelectively(Cow cow);
    void setUserIsActive(Integer uid, Boolean isActive);

    Boolean checkIfExistExceptMe(String uid, String name, String phone, String email, String myId);

    User selectUserById(Integer id);
    User selectUserWhoHas(String uid, String name, String phone, String email);
    List<User> selectAllUsers();
    List<User> selectAllActiveUsers();
}
