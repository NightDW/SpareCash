package com.laidw.mapper;

import com.laidw.entity.user.Cow;
import com.laidw.entity.user.Identity;
import com.laidw.entity.user.Student;
import com.laidw.entity.user.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 测试顺序：2
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserMapperTest {
    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private UserMapper mapper;

    private Student stu1, stu2;
    private Cow cow1, cow2;

    @Test
    public void testInsert() {
        mapper.insertStudent(stu1);
        mapper.insertStudent(stu2);
        mapper.insertCow(cow1);
        mapper.insertCow(cow2);
    }

    @Test
    public void testSelect() {
        System.out.println(mapper.selectUserById(1));
        List<User> list1 = mapper.selectAllUsers();
        for(User u : list1)
            System.out.println(u);
        List<User> list2 = mapper.selectAllActiveUsers();
        for(User u : list2)
            System.out.println(u);
    }

    @Test
    public void testUpdate() {
        Student student = new Student();
        student.setId(1);
        student.setUid(66);
        mapper.updateStudentByIdSelectively(student);
        student.setId(2);
        student.setUid(77);
        student.setName("Stu7");
        student.setPhone("77");
        student.setEmail("77@qq.com");
        student.setIsActive(true);
        student.setSpareCashCoin(10d);
        student.setIdentity(Identity.STUDENT);
        mapper.updateStudentById(student);

        Cow cow = new Cow();
        cow.setId(3);
        cow.setName("Cow8");
        mapper.updateCowByIdSelectively(cow);
        cow.setId(4);
        cow.setUid(99);
        cow.setName("Cow9");
        cow.setPhone("99");
        cow.setEmail("99@qq.com");
        cow.setIsActive(false);
        cow.setSpareCashCoin(80d);
        cow.setIdentity(Identity.COW);
        mapper.updateCowById(cow);
    }

    @Test
    public void testDelete() {
        mapper.deleteUserById(4);
    }

    @Before
    public void initData() {
        stu1 = new Student();
        stu1.setUid(11); stu1.setName("Stu1"); stu1.setPhone("11"); stu1.setEmail("11@qq.com");
        stu1.setIconUrl("http://localhost/stu1.png");
        stu1.setIdentity(Identity.STUDENT); stu1.setIsActive(true);
        stu1.setSpareCashCoin(11d);
        stu1.setGrade(1); stu1.setGender(1); stu1.setAge(1); stu1.setMajor("Software");

        stu2 = new Student();
        stu2.setUid(12); stu2.setName("Stu2"); stu2.setPhone("12"); stu2.setEmail("12@qq.com");
        stu2.setIconUrl("http://localhost/stu2.jpg");
        stu2.setIdentity(Identity.STUDENT); stu2.setIsActive(false);
        stu2.setSpareCashCoin(12d);
        stu2.setGrade(2); stu2.setGender(0); stu2.setAge(2); stu2.setMajor("Music");

        cow1 = new Cow();
        cow1.setUid(21); cow1.setName("Cow1"); cow1.setIconUrl(null);
        cow1.setPhone("21"); cow1.setEmail("21@qq.com");
        cow1.setIdentity(Identity.COW); cow1.setIsActive(false); cow1.setSpareCashCoin(21d);

        cow2 = new Cow();
        cow2.setUid(22); cow2.setName("Cow2"); cow2.setIconUrl(null);
        cow2.setPhone("22"); cow2.setEmail("22@qq.com");
        cow2.setIdentity(Identity.COW); cow2.setIsActive(true); cow2.setSpareCashCoin(22d);
    }
}
