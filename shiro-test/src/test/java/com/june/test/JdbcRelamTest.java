package com.june.test;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.BeforeClass;
import org.junit.Test;

public class JdbcRelamTest {

    DruidDataSource dataSource = new DruidDataSource();
    {
        dataSource.setUrl("jdbc:mysql://localhost:3306/shiroTest");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
    }


    @Test
    public void jdbcRelamTest01() {
        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(dataSource);
        jdbcRealm.setPermissionsLookupEnabled(true);

        //使用自定义 sql 进行查询
        String AuthenticationSql = "select password from test_users where user_name = ?";
        jdbcRealm.setAuthenticationQuery(AuthenticationSql);

        String RoleSql = "select user_roles from test_user_roles where user_name = ?";
        jdbcRealm.setUserRolesQuery(RoleSql);

        String PermissionSql = "select user_permissions from test_roles_permissions where user_roles = ?";
        jdbcRealm.setPermissionsQuery(PermissionSql);


        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(jdbcRealm);

        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("June", "123");
        subject.login(token);

        System.out.println(subject.isAuthenticated());
        subject.checkRole("user");
        subject.checkPermission("user:select");
    }
}
