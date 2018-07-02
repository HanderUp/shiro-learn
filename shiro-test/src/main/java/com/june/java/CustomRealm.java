package com.june.java;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 自定义 shiro Realm
 */
public class CustomRealm extends AuthorizingRealm {

    Map<String, String> userMap = new HashMap<String, String>();
    {
        userMap.put("June", "67fc6fa5bbe5f0fd3a2e4319b26490ef");
        super.setName("customRealm");
    }

    /**
     * 重写 授权方法（角色、权限）
     * @param principalCollection
     * @return
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //1. 从主体传过来的认证信息中获取用户名
        String userName = (String) principalCollection.getPrimaryPrincipal();
        //2. 通过用户名到数据库中查询角色和权限
        Set<String> roles = getRoleByUserName(userName);
        Set<String> permissions = getPermissionsByUserName(userName);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(roles);
        authorizationInfo.setStringPermissions(permissions);
        return authorizationInfo;
    }

    private Set<String> getPermissionsByUserName(String userName) {
        Set<String> permissions = new HashSet<String>();
        permissions.add("user:select");
        permissions.add("user:update");
        return permissions;
    }

    private Set<String> getRoleByUserName(String userName) {
        Set<String> roles = new HashSet<String>();
        roles.add("user");
        roles.add("admin");
        return roles;
    }

    /**
     * 重写 认证方法（用户名、密码）
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        //1. 从主体传过来的认证信息中获取用户名
        String userName = (String) authenticationToken.getPrincipal();
        //2. 通过用户名到数据库中获取认证
        String password = getPasswordByUserName(userName);
        if (password == null) {
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo =  new SimpleAuthenticationInfo(userName,password,"customRealm");
        //设置加密的盐
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("June"));
        return authenticationInfo;
    }

    /**
     * 模拟从数据库查询用户密码
     * @param userName
     * @return
     */
    private String getPasswordByUserName(String userName) {
        return userMap.get(userName);
    }

    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("123456","June");
        System.out.println(md5Hash);
    }
}
