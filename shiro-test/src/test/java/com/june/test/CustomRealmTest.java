package com.june.test;

import com.june.java.CustomRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class CustomRealmTest {

    @Test
    public void customRealmTest01() {
        CustomRealm customRealm = new CustomRealm();

        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(customRealm);

        //shiro加密
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        //设置加密方式
        matcher.setHashAlgorithmName("md5");
        //设置加密次数
        matcher.setHashIterations(1);
        //给自定义Realm设置matcher
        customRealm.setCredentialsMatcher(matcher);

        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("June", "123456");
        subject.login(token);

        System.out.println(subject.isAuthenticated());
//        subject.checkRoles("admin","user");
//        subject.checkPermissions("user:select","user:update");
    }
}
