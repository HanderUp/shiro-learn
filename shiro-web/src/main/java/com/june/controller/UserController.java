package com.june.controller;

import com.june.vo.User;
import javafx.scene.chart.ValueAxis;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    @RequestMapping(value = "/subLogin" ,method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String subLogin(User user) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(),user.getPassword());
        try {
            //设置remindMe参数，实现自动登录
            token.setRememberMe(user.isRemeberMe());
            subject.login(token);
        } catch (AuthenticationException e) {
            return e.getMessage();
        }

        if (subject.hasRole("admin")) {
            return "有admin权限";
        }

        return "无admin权限";
    }

//    @RequiresRoles("admin")
    @RequestMapping(value = "/testRole", method = RequestMethod.GET)
    @ResponseBody
    public String testAnnotationRole() {
        return "testRole success";
    }

//    @RequiresRoles("admin1")
    @RequestMapping(value = "/testRole1", method = RequestMethod.GET)
    @ResponseBody
    public String testAnnotationRole1() {
        return "testRole1 success";
    }

//    @RequiresPermissions({"user:select","user:update"})
    @RequestMapping(value = "/testPermission", method = RequestMethod.GET)
    @ResponseBody
    public String testAnnotationPermission() {
        return "testPermission success";
    }

    @RequestMapping(value = "/testPermission1", method = RequestMethod.GET)
    @ResponseBody
    public String testAnnotationPermission1() {
        return "testPermission1 success";
    }
}
