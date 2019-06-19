package com.laidw.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * SpringSecurity的配置类
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 注入自定义的UserDetailsService
     */
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired private UserDetailsService service;

    /**
     * 进行相关的配置
     * @param http 调用该对象的相关方法即可完成设置
     * @throws Exception 这里不会抛出异常，不用管
     */
    protected void configure(HttpSecurity http) throws Exception {

        //开启登录功能；用户用POST方式发送/login请求时SpringSecurity自动验证用户是否存在
        http.formLogin().loginPage("/login");

        //开启记住我功能，用户勾选记住我后SpringSecurity自动向浏览器写入一个Cookie，用户下次访问会直接登录
        http.rememberMe();

        //开启登出功能，用户发送/logout请求时将会退出登录，回到首页，同时清除掉记住我的Cookie
        http.logout().logoutSuccessUrl("/");

        //关闭CSRF功能
        http.csrf().disable();
    }

    /**
     * 进行相关的配置
     * @param auth 调用该对象的相关方法即可完成设置
     * @throws Exception 这里不会抛出异常，不用管
     */
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        //让SpringSecurity使用我们定义的UserDetailsService，从数据库中获取账户信息
        auth.userDetailsService(service);
    }
}
