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
    
//    @GetMapping("/{id}")
//    public User getByUsername(@PathVariable(name="id") int id) {
//        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Hakemaasi käyttäjää ei löydy"));
//    }
//
//    @GetMapping("/{username}")
//    public User getByUserName(@PathVariable(name="username") String username) {
//        return userRepository.findByUsername(username);
//    }
//
//    @PostMapping
//    public ResponseEntity<?> addOneUser(@RequestBody User user){
//        userRepository.save(user);
//        int id = user.getId();
//        URI location = UriComponentsBuilder.newInstance()
//                .scheme("http")
//                .host("localhost")
//                .port(8080)
//                .path("/users/{id}")
//                .buildAndExpand(id)
//                .toUri();
//        return ResponseEntity.created(location).build();
//    }
//
//    @PutMapping("/{id}")
//    public void editOneUser(@RequestBody User editedUser, @PathVariable int id) {
//        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("'Päivitys ei onnistu. Käyttäjää ei löydy"));
//        editedUser.setId(user.getId());
//        userRepository.save(editedUser);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteOne(@PathVariable int id) {
//        userRepository.deleteById(id);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//
//    }
}
