package com.laidw.mapper;

import com.laidw.entity.user.Cow;
import com.laidw.entity.user.Student;
import com.laidw.entity.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    void insertStudent(Student student);
    void insertCow(Cow cow);

    void deleteUserById(Integer id);

    void updateStudentById(Student stu);
    void updateStudentByIdSelectively(Student stu);
    void updateCowById(Cow cow);
    void updateCowByIdSelectively(Cow cow);
    void updateSpareCashCoin(@Param("uid") Integer uid, @Param("step") Double step);
    void setUserIsActive(@Param("uid") Integer uid, @Param("isActive") Boolean isActive);

    User selectUserById(Integer id);
    User selectUserByName(String name);

    //该方法用于检测UID等数据是否已经被注册，正常情况下uid/name/phone/email参数中只有一个不为空
    //另外，当myId为空时，说明此时是在注册时检测是否重复
    //当myId不为空时，说明此时是在修改用户数据时检测是否重复，数据库中有该用户的数据，在查重时应该排除掉该用户的数据
    User selectUserIfExistExceptMe(@Param("uid") String uid, @Param("name") String name, @Param("phone") String phone, @Param("email") String email, @Param("myId") String myId);

    //该方法用于找回密码，正常情况下4个参数中至少有1个不为空，该方法将以第一个不为空的参数为准来查找用户
    User selectUserWhoHas(@Param("uid") String uid, @Param("name") String name, @Param("phone") String phone, @Param("email") String email);

    List<User> selectAllUsers();
    List<User> selectAllActiveUsers();
}
