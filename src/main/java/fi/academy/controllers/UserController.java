package fi.academy.controllers;

import fi.academy.entities.User;
import fi.academy.exceptions.NotAuthorizedException;
import fi.academy.rowmappers.OneStringRowMapper;
import fi.academy.rowmappers.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
    public List<User> getAllUsers(){
        List<User> result = jdbc.query("select * from users",
                (ResultSet rs, int index) -> {
                String[] ifCompletedtaskNull;
                if (rs.getArray("completedtasks")==null) {
                    ifCompletedtaskNull = new String[0];
                } else {
//                    ifCompletedtaskNull = (String[])rs.getArray("completedtasks").getArray();
                    Object[] o = (Object[])rs.getArray("completedtasks").getArray();
                    ifCompletedtaskNull = Arrays.asList(o).toArray(new String[0]);
                }
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("role"),
                            rs.getInt("points"),
                            rs.getInt("groupid"),
                            ifCompletedtaskNull,
                            rs.getInt("contactpersonuserid"),
                            rs.getString("authid"));
                });
        return result;
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getOneUserById(@PathVariable String id, Principal principal) {
        RowMapper<User> userRowMapper = new UserRowMapper();
        String sql = "SELECT * FROM users WHERE authid=?";
        try {
            String queryId = id.equals(principal.getName()) ? id : null;
            System.out.println(queryId);
            User user = jdbc.queryForObject(sql, userRowMapper, queryId);
            System.out.println(user);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } catch (NotAuthorizedException err) {
            throw err;
        } catch (EmptyResultDataAccessException dataAccessException) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("User not found with id: " + id);
        }
    }

//    @GetMapping("/{id}/id")
//    public User getOneUserById(@PathVariable String id, Principal principal) {
//        System.out.println("Käyttäjä " + principal.getName());
//        System.out.println("ID " + id);
//        System.out.println(principal.getName().getClass());
//        String queryId = id.equals(principal.getName()) ? id : null;
//        System.out.println("QueryId " + queryId);
//        try{
//            RowMapper<User> userRowMapper = new UserRowMapper();
//            String sql = "SELECT * FROM users WHERE testid=?";
//            return jdbc.queryForObject(sql, userRowMapper,queryId);
//        } catch (NotAuthorizedException err){
//            throw err;
//        }
//    }
    
    public User getOneUserByUsername(@RequestParam String username) {
        RowMapper<User> userRowMapper = new UserRowMapper();
        String sql = "SELECT * FROM users WHERE username=?";
        return jdbc.queryForObject(sql, userRowMapper, username);
    }
    
    @GetMapping("/{id}/completedtasks")
    public String[] getCompletedTasksForUser(@PathVariable String id) {
        RowMapper<String[]> completedTasksRowMapper = new OneStringRowMapper("completedtasks");
        String sql = "SELECT completedtasks FROM users WHERE authid=?";
        return jdbc.queryForObject(sql, completedTasksRowMapper, id);
    }
    
    @PostMapping()
    public User insertUser(@RequestBody User user) {
        KeyHolder kh = new GeneratedKeyHolder();
        String sql = "INSERT INTO users (username, role, points, groupid, completedtasks, contactpersonuserid, authid) values (?, ?, ?, ?, ?, ?,?)";
        
        PreparedStatementCreator preparedStatementCreator = connection -> {
            PreparedStatement preparedStatement = connection
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getRole());
            preparedStatement.setInt(3, 0);
            preparedStatement.setInt(4, user.getGroupid());
            preparedStatement.setArray(5, connection.createArrayOf("text", user.getCompletedtasks()));
            preparedStatement.setInt(6, user.getContactpersonuserid());
            preparedStatement.setString(7, user.getAuthid());
            return preparedStatement;
        };
        
        jdbc.update(preparedStatementCreator, kh);
        Integer generatedId = (Integer)kh.getKeys().get("id");
        user.setId(generatedId);
        return user;
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
    
    @PutMapping("/{id}/points")
    public int updateUserPoints(@PathVariable String id, @RequestBody User user) {
        String sql = "UPDATE users SET points = ? WHERE authid=?";
        return jdbc.update(sql, new Object[]{user.getPoints(), id});
    }
    
    @PutMapping("/{id}/groupid")
    public int updateUserGroupid(@PathVariable String id, @RequestBody User user) {
        String sql = "UPDATE users SET groupid = ? WHERE authid=?";
        return jdbc.update(sql, new Object[]{user.getGroupid(), id});
    }
    
    @PutMapping("/{id}/completed")
    public String updateUserCompletedtasks(@PathVariable String id, @RequestBody User user) {
        KeyHolder kh = new GeneratedKeyHolder();
        String sql = "UPDATE users SET completedtasks = ? WHERE authid=?";
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
            preparedStatement.setString(2, id);
            return preparedStatement;
        };
        
        jdbc.update(preparedStatementCreator, kh);
        return kh.getKeys().toString();
    }
    
    @PutMapping("/{id}/contact")
    public int updateUserContactpersonuserid(@PathVariable String id, @RequestBody User user) {
        String sql = "UPDATE users SET contactpersonuserid = ? WHERE authid=?";
        return jdbc.update(sql, new Object[]{user.getContactpersonuserid(), id});
    }

}
