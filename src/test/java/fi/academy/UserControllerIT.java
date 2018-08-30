package fi.academy;

import fi.academy.entities.User;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIT {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    public void contextLoads() throws JSONException {
        String response = this.restTemplate.getForObject("/users/1/id", String.class);
        JSONAssert.assertEquals("{}", response, false);
    }
}
