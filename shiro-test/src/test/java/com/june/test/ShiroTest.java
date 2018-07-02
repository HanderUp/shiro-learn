package com.june.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

public class ShiroTest {

    //用于管理从数据库获取的用户数据
    SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();
    //模拟查询到的用户
    @Before
    public void addUser() {
        simpleAccountRealm.addAccount("June","123456");
    }

    @Test
    public void shiroTest01() {
        //1.创建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        //将 Realm 加入 SecurityManager 进行管理
        defaultSecurityManager.setRealm(simpleAccountRealm);

        //2.获取主体
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        //3.获取用户Token，模拟页面传过来的用户登录信息
        UsernamePasswordToken token = new UsernamePasswordToken("June","123456");
        subject.login(token);

        //4.判断是否验证成功
        System.out.println(subject.isAuthenticated());

    }
}
