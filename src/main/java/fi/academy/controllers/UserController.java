package fi.academy.controllers;

import fi.academy.entities.User;
import fi.academy.rowmappers.OneStringRowMapper;
import fi.academy.rowmappers.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private JdbcTemplate jdbc;
    
    public UserController(@Autowired JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }
    
    @GetMapping()
    public List<User> users() {
        List<User> result = jdbc.query("select * from users",
                (ResultSet rs, int index) -> {
                String[] ifCompletedtaskNull;
                if (rs.getArray("completedtasks")==null) {
                    ifCompletedtaskNull = new String[0];
                } else {
                    ifCompletedtaskNull = (String[])rs.getArray("completedtasks").getArray();
                }
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("role"),
                            rs.getInt("points"),
                            rs.getInt("groupId"),
                            ifCompletedtaskNull,
                            rs.getInt("contactpersonuserid"));
                });
        return result;
    }
    
    @GetMapping("/{id}/id")
    public User getOneUserById(@PathVariable Integer id) {
        RowMapper<User> userRowMapper = new UserRowMapper();
        String sql = "SELECT * FROM users WHERE id=?";
        return jdbc.queryForObject(sql, userRowMapper, id);
    }
    
    @GetMapping("/{username}/username")
    public User getOneUserByUsername(@PathVariable String username) {
        RowMapper<User> userRowMapper = new UserRowMapper();
        String sql = "SELECT * FROM users WHERE username=?";
        return jdbc.queryForObject(sql, userRowMapper, username);
    }
    
    @GetMapping("/{id}/completedtasks")
    public String[] getCompletedTasksForUser(@PathVariable Integer id) {
        RowMapper<String[]> completedTasksRowMapper = new OneStringRowMapper("completedtasks");
        String sql = "SELECT completedtasks FROM users WHERE id=?";
        return jdbc.queryForObject(sql, completedTasksRowMapper, id);
    }
    
    @PostMapping()
    public String insertUser(@RequestBody User user) {
        KeyHolder kh = new GeneratedKeyHolder();
        String sql = "INSERT INTO users (username, role, points, groupid, completedtasks, contactpersonuserid) values (?, ?, ?, ?, ?, ?)";
        
        PreparedStatementCreator preparedStatementCreator = connection -> {
            PreparedStatement preparedStatement = connection
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getRole());
            preparedStatement.setInt(3, 0);
            preparedStatement.setInt(4, user.getGroupId());
            preparedStatement.setArray(5, connection.createArrayOf("text", user.getCompletedtasks()));
            preparedStatement.setInt(6, user.getContactpersonuserid());
            return preparedStatement;
        };
        
        jdbc.update(preparedStatementCreator, kh);
        return kh.getKeys().toString();
    }
    
    @PutMapping("/{id}/username")
    public int updateUsername(@PathVariable Integer id, @RequestBody User user) {
        String sql = "UPDATE users SET username = ? WHERE id=?";
        return jdbc.update(sql, new Object[]{user.getUsername(), id});
    }
    
    @PutMapping("/{id}/role")
    public int updateUserRole(@PathVariable Integer id, @RequestBody User user) {
        String sql = "UPDATE users SET role = ? WHERE id=?";
        return jdbc.update(sql, new Object[]{user.getRole(), id});
    }
    
    @PutMapping("/id}/points")
    public int updateUserPoints(@PathVariable Integer id, @RequestBody User user) {
        String sql = "UPDATE users SET points = ? WHERE id=?";
        return jdbc.update(sql, new Object[]{user.getPoints(), id});
    }
    
    @PutMapping("/id}/groupid")
    public int updateUserGroupid(@PathVariable Integer id, @RequestBody User user) {
        String sql = "UPDATE users SET groupid = ? WHERE id=?";
        return jdbc.update(sql, new Object[]{user.getGroupId(), id});
    }
    
    @PutMapping("/{id}/completed")
    public String updateUserCompletedtasks(@PathVariable Integer id, @RequestBody User user) {
        KeyHolder kh = new GeneratedKeyHolder();
        String sql = "UPDATE users SET completedtasks = ? WHERE id=?";
        ArrayList<String> existingCompletedTasks = new ArrayList<>(Arrays.asList(getCompletedTasksForUser(id)));
        for (String task : user.getCompletedtasks()) {
            existingCompletedTasks.add(task);
        }
        
        String[] updatedTasks = new String[existingCompletedTasks.size()];
        updatedTasks = existingCompletedTasks.toArray(updatedTasks);
    
        String[] finalUpdatedTasks = updatedTasks;
        PreparedStatementCreator preparedStatementCreator = connection -> {
            PreparedStatement preparedStatement = connection
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setArray(1, connection.createArrayOf("text", finalUpdatedTasks));
            preparedStatement.setInt(2, id);
            return preparedStatement;
        };
        
        jdbc.update(preparedStatementCreator, kh);
        return kh.getKeys().toString();
    }
    
    @PutMapping("/id}/contact")
    public int updateUserContactpersonuserid(@PathVariable Integer id, @RequestBody User user) {
        String sql = "UPDATE users SET contactpersonuserid = ? WHERE id=?";
        return jdbc.update(sql, new Object[]{user.getContactpersonuserid(), id});
    }

}
