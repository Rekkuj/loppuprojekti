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
    public User getOneUser(@PathVariable int id){
        RowMapper<User> userRowMapper = new UserRowMapper();
        String sql = "SELECT * FROM users WHERE id=?";
        User oneUser = jdbc.queryForObject(sql, userRowMapper, id);
        return oneUser;
    }
    
    @PostMapping()
    public String insertUser(@RequestBody User user){
        KeyHolder kh = new GeneratedKeyHolder();
        String sql = "INSERT INTO users (username, role, points, groupId) values (?, ?, ?, ?)";
    
        PreparedStatementCreator preparedStatementCreator = connection -> {
            PreparedStatement preparedStatement = connection
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getUsername());
            return preparedStatement;
        };
        
        jdbc.update(preparedStatementCreator, kh);
        return kh.getKeys().toString();
    }
    
    @PutMapping("/{id}")
    public int updateUser(@PathVariable int id, @RequestBody User user) {
        String sql = "UPDATE users SET username = ? WHERE id=?";
        return jdbc.update(sql, new Object[] {user.getUsername(), id});
    }
    
    @DeleteMapping("/{id}")
    public int deleteUserById(@PathVariable int id) {
        String sql = "DELETE FROM users WHERE id=?";
        return jdbc.update(sql, new Object[] {id});
    }
}
