package com.june.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class iniRelamTest {

    @Test
    public void iniRelamTest01() {
        //1.创建 iniRealm ，从resource中获取配置文件
        IniRealm iniRealm = new IniRealm("classpath:User.ini");

        //2.创建 SecurityManager
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(iniRealm);

        //3.获取 Subject
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        //4.验证用户
        UsernamePasswordToken token = new UsernamePasswordToken("June","123456");
        subject.login(token);

        System.out.println(subject.isAuthenticated());

        subject.checkRoles("admin","user");
        subject.checkPermissions("user:delete","user:update");
    }
}
