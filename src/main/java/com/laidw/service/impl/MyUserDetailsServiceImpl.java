package com.laidw.service.impl;

import com.laidw.entity.user.User;
import com.laidw.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 这个Service实现类是和验证用户相关的
 * SpringSecurity将会调用该类中的相关方法来获取数据库中的账户信息
 * 然后通过对比数据库中的账户信息与用户提交的账户信息来确定是否登录成功
 */
@Service
public class MyUserDetailsServiceImpl implements UserDetailsService {

    @SuppressWarnings({"SpringJavaAutowiredFieldsWarningInspection", "SpringJavaInjectionPointsAutowiringInspection"})
    @Autowired private UserMapper mapper;


    /**
     * SpringSecurity通过该方法来获取用户名对应的真实信息，包括密码等
     * @param username 用户登录时填写的用户名，我们需要根据该用户名到数据库中查出真实的用户数据
     * @return 用户的真实数据
     * @throws UsernameNotFoundException 用户不存在时抛出的异常
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = mapper.selectUserByName(username);
        if(user == null)
            throw new UsernameNotFoundException("用户名不存在！");

        //我们的User类已经实现了UserDetails接口，因此直接把User对象返回即可
        return user;
    }
}
