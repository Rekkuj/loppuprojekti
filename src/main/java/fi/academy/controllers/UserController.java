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
    public List<User> getAllUsers(Principal principal) {
        RowMapper<User> userRowMapper = new UserRowMapper();
        String sql = "SELECT * FROM users WHERE authid=?";
        try {
            //TODO hae prinsipalilla hlö, tsekkaa onko ope tai sama
            String subQuery = "SELECT * FROM users where authid=?";
            User authenticatedPrincipal = jdbc.queryForObject(subQuery, userRowMapper, principal.getName());
            if (authenticatedPrincipal.getRole().equals("CHIEF")) {
                List<User> result = jdbc.query("select * from users",
                        (ResultSet rs, int index) -> {
                            String[] ifCompletedmissionsNull;
                            if (rs.getArray("completedmissions") == null) {
                                ifCompletedmissionsNull = new String[0];
                            } else {
//                    ifCompletedmissionsNull = (String[])rs.getArray("completedmissions").getArray();
                                Object[] o = (Object[]) rs.getArray("completedmissions").getArray();
                                ifCompletedmissionsNull = Arrays.asList(o).toArray(new String[0]);
                            }
                            return new User(
                                    rs.getInt("id"),
                                    rs.getString("username"),
                                    rs.getString("role"),
                                    rs.getInt("points"),
                                    rs.getInt("groupid"),
                                    ifCompletedmissionsNull,
                                    rs.getInt("contactpersonuserid"),
                                    rs.getString("authid"));
                        });
                return result;
            } else {
                throw new NotAuthorizedException("Not authorized");
            }
        } catch (NullPointerException e) {
            throw new NullPointerException();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneUserById(@PathVariable String id, Principal principal) {
        RowMapper<User> userRowMapper = new UserRowMapper();
        String sql = "SELECT * FROM users WHERE authid=?";
        try {
            String subQuery = "SELECT * FROM users where authid=?";
            User authenticatedPrincipal = jdbc.queryForObject(subQuery, userRowMapper, principal.getName());
            String queryId;
            if (authenticatedPrincipal.getRole().equals("CHIEF") || id.equals(principal.getName())) {
                queryId = id;
            } else {
                queryId = null;
            }
            System.out.println(queryId);
            User user = jdbc.queryForObject(sql, userRowMapper, queryId);
            System.out.println(user);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } catch (NotAuthorizedException err) {
            return ResponseEntity.status(403).body("Not authorized");
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

    @GetMapping("/user/{username}")
    public User getOneUserByUsername(@PathVariable String username, Principal principal) {
        RowMapper<User> userRowMapper = new UserRowMapper();
        String sql = "SELECT * FROM users WHERE username=?";
        //TODO hae prinsipalilla hlö, tsekkaa onko ope tai sama
        String subQuery = "SELECT * FROM users where authid=?";
        User authenticatedPrincipal = jdbc.queryForObject(subQuery, userRowMapper, principal.getName());
        if (authenticatedPrincipal.getRole().equals("CHIEF")) {
            return jdbc.queryForObject(sql, userRowMapper, username);
        } else {
            throw new NotAuthorizedException("Not authorized");
        }
    }

    @GetMapping("/{id}/completedmissions")
    public String[] getCompletedmissionsForUser(@PathVariable String id, Principal principal) {
        RowMapper<String[]> completedMissionsRowMapper = new OneStringRowMapper("completedmissions");
        String sql = "SELECT completedmissions FROM users WHERE authid=?";
        RowMapper<User> userRowMapper = new UserRowMapper();
        String subQuery = "SELECT * FROM users where authid=?";
        User authenticatedPrincipal = jdbc.queryForObject(subQuery, userRowMapper, principal.getName());
        if (authenticatedPrincipal.getRole().equals("CHIEF") || id.equals(principal.getName())) {
            return jdbc.queryForObject(sql, completedMissionsRowMapper, id);
        } else {
            throw new NotAuthorizedException("Not authorized");
        }
    }

    @PostMapping()
    public User insertUser(@RequestBody User user, Principal principal) {
        KeyHolder kh = new GeneratedKeyHolder();
        String sql = "INSERT INTO users (username, role, points, groupid, completedmissions, contactpersonuserid, authid) values (?, ?, ?, ?, ?, ?,?)";
        RowMapper<User> userRowMapper = new UserRowMapper();
        String subQuery = "SELECT * FROM users where authid=?";
        User authenticatedPrincipal = jdbc.queryForObject(subQuery, userRowMapper, principal.getName());
        if (authenticatedPrincipal.getRole().equals("CHIEF")) {
            PreparedStatementCreator preparedStatementCreator = connection -> {
                PreparedStatement preparedStatement = connection
                        .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setString(2, user.getRole());
                preparedStatement.setInt(3, 0);
                preparedStatement.setInt(4, user.getGroupid());
                preparedStatement.setArray(5, connection.createArrayOf("text", user.getCompletedmissions()));
                preparedStatement.setInt(6, user.getContactpersonuserid());
                preparedStatement.setString(7, user.getAuthid());
                return preparedStatement;
            };

            jdbc.update(preparedStatementCreator, kh);
            Integer generatedId = (Integer) kh.getKeys().get("id");
            user.setId(generatedId);
            return user;
        } else {
            throw new NotAuthorizedException("Not authorized");
        }
    }


    @PutMapping("/{id}/username")
    public int updateUsername(@PathVariable Integer id, @RequestBody User user, Principal principal) {
        if (principal.getName() != null) {
            String sql = "UPDATE users SET username = ? WHERE id=?";
            return jdbc.update(sql, new Object[]{user.getUsername(), id});
        } else {
            throw new NotAuthorizedException("Not authorized");
        }
    }

    @PutMapping("/{id}/role")
    public int updateUserRole(@PathVariable Integer id, @RequestBody User user, Principal principal) {
        if (principal.getName() != null) {
            String sql = "UPDATE users SET role = ? WHERE id=?";
            return jdbc.update(sql, new Object[]{user.getRole(), id});
        } else {
            throw new NotAuthorizedException("Not authorized");
        }
    }

    @PutMapping("/{id}/points")
    public int updateUserPoints(@PathVariable String id, @RequestBody User user, Principal principal) {
        if (principal.getName() != null) {
            String sql = "UPDATE users SET points = ? WHERE authid=?";
            return jdbc.update(sql, new Object[]{user.getPoints(), id});
        } else {
            throw new NotAuthorizedException("Not authorized");
        }
    }

    @PutMapping("/{id}/groupid")
    public int updateUserGroupid(@PathVariable String id, @RequestBody User user, Principal principal) {
        if (principal.getName() != null) {
            String sql = "UPDATE users SET groupid = ? WHERE authid=?";
            return jdbc.update(sql, new Object[]{user.getGroupid(), id});
        } else {
            throw new NotAuthorizedException("Not authorized");
        }
    }

    @PutMapping("/{id}/completed")
    public String updateUserCompletedmissions(@PathVariable String id, @RequestBody User user, Principal principal) {
        if (principal.getName() != null) {
            KeyHolder kh = new GeneratedKeyHolder();
        String sql = "UPDATE users SET completedmissions = ? WHERE authid=?";
        ArrayList<String> existingCompletedmissions = new ArrayList<>(Arrays.asList(getCompletedmissionsForUser(id, principal)));
        for (String task : user.getCompletedmissions()) {
            existingCompletedmissions.add(task);
        }

        String[] updatedMissions = new String[existingCompletedmissions.size()];
        updatedMissions = existingCompletedmissions.toArray(updatedMissions);

        String[] finalUpdatedMissions = updatedMissions;
        PreparedStatementCreator preparedStatementCreator = connection -> {
            PreparedStatement preparedStatement = connection
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setArray(1, connection.createArrayOf("text", finalUpdatedMissions));
            preparedStatement.setString(2, id);
            return preparedStatement;
        };

        jdbc.update(preparedStatementCreator, kh);
        return kh.getKeys().toString();
        } else {
            throw new NotAuthorizedException("Not authorized");
        }
    }

    @PutMapping("/{id}/contact")
    public int updateUserContactpersonuserid(@PathVariable String id, @RequestBody User user, Principal principal) {
        if (principal.getName() != null) {
            String sql = "UPDATE users SET contactpersonuserid = ? WHERE authid=?";
            return jdbc.update(sql, new Object[]{user.getContactpersonuserid(), id});
        } else {
            throw new NotAuthorizedException("Not authorized");
        }
    }

}
