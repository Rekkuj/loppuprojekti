package fi.academy.controllers;

import fi.academy.controllers.UserController;
import fi.academy.LoppuprojektiApplication;
import fi.academy.entities.User;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LoppuprojektiApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    
    @LocalServerPort
    private int port;
    private String response;
    private String actual;
    
    @Autowired
    UserController userController;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    HttpHeaders headers = new HttpHeaders();
    
    private String urlWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
    
    /*Check that context loads and responsebody contains user "Jermu"*/
    @Test
    public void contextLoads(){

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        
        response = this.restTemplate.getForObject("/users", String.class);
        ResponseEntity<String> responseEntity = restTemplate.exchange(urlWithPort("/users"), HttpMethod.GET, entity, String.class);
        actual = responseEntity.getBody().toString();
        assertTrue(actual.contains("{\"id\":1,\"username\":\"Jermu\",\"role\":\"Testaaja\",\"points\":2000,\"groupid\":1,\"completedtasks\":[\"Himmeli\",\"Helpperi\"],\"contactpersonuserid\":1}"));
    }
    
    /*Check that context loads and responsebody contains user "Jermu"*/
    @Test
    public void getAllUsersTest(){
        ResponseEntity<List<User>> responseEntity = restTemplate.exchange(urlWithPort("/users"), HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {
        });
        List<User> user = responseEntity.getBody();
        assertTrue(user.size()==6);
    }
    
    /* Call GET by username */
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
    
    /* Call GET by id */
    @Test
    public void getOneUserByIdTest() throws JSONException {
        response = this.restTemplate.getForObject("/users/4/id", String.class);
        JSONAssert.assertEquals("{\n" +
                "    \"username\": \"Rekku\",\n" +
                "    \"role\": \"Testaaja\",\n" +
                "    \"points\": 2000,\n" +
                "    \"groupid\": 2,\n" +
                "    \"completedtasks\": [\"Orvokki\", \"Ruiskaunokki\"],\n" +
                "    \"contactpersonuserid\": 1\n" +
                "}", response, false);
    }

    /* Check if Jermu user's completedtasks equals Himmeli and Helpperi */
    @Test
    public void getCompletedTasksForUserTest() throws JSONException {
        response = this.restTemplate.getForObject("/users/1/completedtasks", String.class);
        JSONAssert.assertEquals("[\"Himmeli\", \"Helpperi\"]", response, false);
    }
    
    /* Check if creating a user is successful*/
    @Test
    public void insertUserTest() {
        User user = new User("Kettu", "Kekkuloija", 500, 1, null, 3);
        
        ResponseEntity<User> responseEntity = restTemplate.postForEntity(urlWithPort("/users"), user, User.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        
        /* Check location for creating testLocation*/
        String location = responseEntity.getHeaders().getLocation().getPath();
        String testLocation = URI.create(location).getPath();
        responseEntity = restTemplate.getForEntity(testLocation, User.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        User insertedUser = responseEntity.getBody();
        assertTrue(insertedUser.equals(user));
    }
    
    @Test
    public void UpdateUsernameTest() throws Exception {
        User firstUser = getFirstUserFromDBTest();
        String url = urlWithPort("/users" + firstUser.getUsername() + "/username");
        String originalUsername = String.valueOf(userController.getOneUserByUsername(firstUser.getUsername()));
        String updatedUsername = originalUsername + "_updated";
        firstUser.setUsername(updatedUsername);
        RequestEntity<User> requestEntity = new RequestEntity<>(firstUser, HttpMethod.PUT, new URI(url));
        ResponseEntity<User> responseEntity = restTemplate.exchange(requestEntity, User.class);
        assertEquals(updatedUsername, firstUser.getUsername());
    }
    
    private User getFirstUserFromDBTest() {
        List<User> allUsers = userController.getAllUsers();
        return allUsers.isEmpty() ? null : allUsers.get(0);
    }
}
