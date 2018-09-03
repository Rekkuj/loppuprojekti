package fi.academy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@SpringBootApplication
@EnableJpaRepositories
@RestController
public class LoppuprojektiApplication {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @RequestMapping("api/user")
    public Principal user(Principal principal) {
//        System.out.println("Tuli pyynto: " + principal.toString());
        return principal;
    }

    public static void main(String[] args) {
        SpringApplication.run(LoppuprojektiApplication.class, args);
    }
}
