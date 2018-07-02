package com.june.dao.impl;

import com.june.dao.UserDao;
import com.june.vo.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class UserDaoImpl implements UserDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据用户名返回用户
     * @param userName
     * @return
     */
    public User getUserByUserName(String userName) {
        String sql = "select username,password from users where username = ?";
        List<User> list = jdbcTemplate.query(sql, new String[]{userName}, new RowMapper<User>() {
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                return user;
            }
        });

        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 根据用户名返回角色
     * @param userName
     * @return
     */
    public List<String> getRolesByUserName(String userName) {
        String sql = "select role_name from user_roles where username = ?";
        return jdbcTemplate.query(sql, new String[]{userName}, new RowMapper<String>() {
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("role_name");
            }
        });
    }

    /**
     * 根据用户名返回权限
     * @param userName
     * @return
     */
    public List<String> getPermissionsByUserName(String userName) {
        String sql = "SELECT t1.permission from roles_permissions t1 LEFT JOIN user_roles t2 ON t1.role_name = t2.role_name WHERE username = ?";
        return jdbcTemplate.query(sql, new String[]{userName}, new RowMapper<String>() {
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("permission");
            }
        });
    }
}
