package fi.academy;

import fi.academy.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.util.List;

@SpringBootApplication
public class LoppuprojektiApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoppuprojektiApplication.class, args);
    }
    
//    @Bean
//    CommandLineRunner alkuun(@Autowired JdbcTemplate jdbcTemplate) {
//        return args -> {
//            List<User> result = jdbcTemplate.query("select * from users",
//                    (ResultSet rs, int index)->{
//                        return new User(
//                                rs.getInt("id"),
//                                rs.getString("username"));
//                    });
//            System.out.println(result);
//        };
//    }
}
