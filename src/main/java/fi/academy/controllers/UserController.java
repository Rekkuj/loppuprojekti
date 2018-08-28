package fi.academy.controllers;

import fi.academy.entities.User;
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
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("role"),
                            rs.getInt("points"),
                            rs.getInt("groupId"));
                });
        return result;
    }
    
    @GetMapping("/{id}")
    public User getOneUserById(@PathVariable Integer id) {
        RowMapper<User> userRowMapper = new UserRowMapper();
        String sql = "SELECT * FROM users WHERE id=?";
        return jdbc.queryForObject(sql, userRowMapper, id);
    }
    
    @GetMapping("/username/{username}")
    public User getOneUserByUsername(@PathVariable String username) {
        RowMapper<User> userRowMapper = new UserRowMapper();
        String sql = "SELECT * FROM users WHERE username=?";
        return jdbc.queryForObject(sql, userRowMapper, username);
    }
    
    @PostMapping()
    public String insertUser(@RequestBody User user) {
        KeyHolder kh = new GeneratedKeyHolder();
        String sql = "INSERT INTO users (username, role, points, groupid) values (?, ?, ?, ?)";
        
        PreparedStatementCreator preparedStatementCreator = connection -> {
            PreparedStatement preparedStatement = connection
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getRole());
            preparedStatement.setInt(3, user.getPoints());
            preparedStatement.setInt(4, user.getGroupId());
            return preparedStatement;
        };
        
        jdbc.update(preparedStatementCreator, kh);
        return kh.getKeys().toString();
    }
    
    @PutMapping("/{id}")
    public int updateUsername(@PathVariable Integer id, @RequestBody User user) {
        String sql = "UPDATE users SET username = ? WHERE id=?";
        return jdbc.update(sql, new Object[]{user.getUsername(), id});
    }
    
    @PutMapping("/user/{id}")
    public int updateUserPoints(@PathVariable Integer id, @RequestBody User user) {
        String sql = "UPDATE users SET points = ? WHERE id=?";
        return jdbc.update(sql, new Object[]{user.getPoints(), id});
    }
    
    @PutMapping("/{id}/completed")
    public String updateUserCompletedtasks(@PathVariable Integer id, @RequestBody User user) {
        KeyHolder kh = new GeneratedKeyHolder();
        String sql = "UPDATE users SET completedtasks = ? WHERE id=?";
    
        PreparedStatementCreator preparedStatementCreator = connection -> {
            PreparedStatement preparedStatement = connection
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setArray(1, connection.createArrayOf("text", user.getCompletedtasks()));
            preparedStatement.setInt(2, id);
            return preparedStatement;
        };
    
        jdbc.update(preparedStatementCreator, kh);
        return kh.getKeys().toString();
    }
    
    @PutMapping("/disableuser/{id}")
    public int deleteUserById(@PathVariable Integer id) {
        String sql = "DELETE FROM users WHERE id=?";
        return jdbc.update(sql, new Object[]{id});
    }
}
