package com.june.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class PermissionOrFilter extends AuthorizationFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        Subject subject = getSubject(servletRequest, servletResponse);
        String[] permissions = (String[]) o;
        /*if (permissions == null || permissions.length == 0) {
            return true;
        }*/
        for (String permission : permissions) {
            subject.isPermitted(permission);
            return true;
        }
        return false;
    }
}
