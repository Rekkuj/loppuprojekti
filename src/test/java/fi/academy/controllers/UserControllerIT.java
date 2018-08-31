package fi.academy.controllers;

import fi.academy.LoppuprojektiApplication;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LoppuprojektiApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIT {
    
    @LocalServerPort
    private int port;
    private String response;
    private String actual;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    HttpHeaders headers = new HttpHeaders();
    
    @Test
    public void contextLoads(){
    
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        
        response = this.restTemplate.getForObject("/groups", String.class);
        
        ResponseEntity<String> responseEntity = restTemplate.exchange(urlWithPort("/groups"), HttpMethod.GET, entity, String.class);
        actual = responseEntity.getBody().toString();
        assertTrue(actual.contains("groupname"));
    }
    
    private String urlWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
    
    @Test
    public void getOneUserByUsernameTest() throws JSONException {
        response = this.restTemplate.getForObject("/users/Rekku/username", String.class);
        JSONAssert.assertEquals("{\n" +
                "    \"username\": \"Rekku\",\n" +
                "    \"role\": \"Testaaja\",\n" +
                "    \"points\": 2000,\n" +
                "    \"groupid\": 2,\n" +
                "    \"completedtasks\": [\"Orvokki\", \"Ruiskaunokki\"],\n" +
                "    \"contactpersonuserid\": 1\n" +
                "}", response, false);
    }

    @Test
    public void getCompletedTasksForUserTest() throws JSONException {
        response = this.restTemplate.getForObject("/users/1/completedtasks", String.class);
        JSONAssert.assertEquals("{[]}", response, false);
    }
    
}
