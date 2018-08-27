package fi.academy.controllers;

import fi.academy.entities.User;
import fi.academy.exceptions.UserNotFoundException;
import fi.academy.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.sql.ResultSet;
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
                        rs.getString("username"));
                
            });
        return result;
    }
    

}
