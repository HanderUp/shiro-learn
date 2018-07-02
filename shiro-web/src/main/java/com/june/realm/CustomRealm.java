package com.june.realm;

import com.june.dao.UserDao;
import com.june.vo.User;
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

import javax.annotation.Resource;
import java.util.*;

/**
 * 自定义 shiro Realm
 */
public class CustomRealm extends AuthorizingRealm {

    @Resource
    private UserDao userDao;

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
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(userName));
        return authenticationInfo;
    }

    /**
     * 从数据库查询用户密码
     * @param userName
     * @return
     */
    private String getPasswordByUserName(String userName) {
        User user = userDao.getUserByUserName(userName);
        if (user != null) {
            return user.getPassword();
        }
        return null;
    }

    /**
     * 从数据库查询用户权限
     * @param userName
     * @return
     */
    private Set<String> getPermissionsByUserName(String userName) {
        System.out.println("从数据库中获取权限数据");
        List<String> list = userDao.getPermissionsByUserName(userName);
        Set<String> permissions = new HashSet<String>(list);
        return permissions;
    }

    /**
     * 从数据库查询用户角色
     * @param userName
     * @return
     */
    private Set<String> getRoleByUserName(String userName) {
        System.out.println("从数据库中获取角色数据");
        List<String> list = userDao.getRolesByUserName(userName);
        Set<String> roles = new HashSet<String>(list);
        return roles;
    }

    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("123456","June");
        System.out.println(md5Hash);
    }
}
