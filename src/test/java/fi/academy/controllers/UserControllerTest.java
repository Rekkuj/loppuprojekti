package fi.academy.controllers;

import fi.academy.LoppuprojektiApplication;
import fi.academy.entities.AccessToken;
import fi.academy.entities.AuthenticationQuery;
import fi.academy.entities.HeaderRequestInterceptor;
import fi.academy.entities.User;
import org.json.JSONException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LoppuprojektiApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @LocalServerPort
    private int port;
    private String response;
    private String actual;

    static AccessToken accessToken;

    @Autowired
    JdbcTemplate jdbc;

    @Autowired
    UserController userController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void beforeEachTest() {
        this.restTemplate = restTemplate;
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(new HeaderRequestInterceptor("Authorization", "Bearer " + accessToken.getAccess_token()));

        restTemplate.getRestTemplate().setInterceptors(interceptors);
    }

    HttpHeaders headers = new HttpHeaders();

    private String urlWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    /*Check that context loads and responsebody contains user "Jermu"*/
    @Test
    public void getJermuUser(){
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        response = this.restTemplate.getForObject("/users", String.class);
        ResponseEntity<String> responseEntity = restTemplate.exchange(urlWithPort("/users"), HttpMethod.GET, entity, String.class);
        actual = responseEntity.getBody().toString();
        assertTrue(actual.contains("{\"id\":1,\"username\":\"Jermu\",\"role\":\"CHIEF\",\"points\":2000,\"groupid\":1,\"completedmissions\":[\"Himmeli\",\"Helpperi\"],\"contactpersonuserid\":1,\"authid\":\"auth0|5b87943afe13090f5ffd652b\"}"));
    }

    /*Check that context loads and responsebody contains user "Jermu"*/
    @Test
    public void getAllUsersTest(){
        ResponseEntity<List<User>> responseEntity = restTemplate.exchange(urlWithPort("/users"), HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {
        });
        List<User> user = responseEntity.getBody();
//        Integer lkm = jdbc.queryForObject("select count(*) from users", Integer.class);
//        System.out.println("\n***** USER LKM " + lkm);
        assertTrue(user.size()==6);
    }

    /* Call GET by username */
    @Test
    public void getOneUserByUsernameTest() throws JSONException {
        response = this.restTemplate.getForObject("/users/user/Rekku", String.class);
        JSONAssert.assertEquals("{\n" +
                "    \"username\": \"Rekku\",\n" +
                "    \"role\": \"Testaaja\",\n" +
                "    \"points\": 2000,\n" +
                "    \"groupid\": 2,\n" +
                "    \"completedmissions\": [\"Orvokki\", \"Ruiskaunokki\"],\n" +
                "    \"contactpersonuserid\": 1\n" +
                "}", response, false);
    }

    /* Call GET by id */
    @Test
    public void getOneUserByIdTest() throws JSONException {
        response = this.restTemplate.getForObject("/users/4", String.class);
        JSONAssert.assertEquals("{\n" +
                "    \"username\": \"Rekku\",\n" +
                "    \"role\": \"Testaaja\",\n" +
                "    \"points\": 2000,\n" +
                "    \"groupid\": 2,\n" +
                "    \"completedmissions\": [\"Orvokki\", \"Ruiskaunokki\"],\n" +
                "    \"contactpersonuserid\": 1\n" +
                "}", response, false);
    }

    /* Check if Jermu user's completedtasks equals Himmeli and Helpperi */
    @Test
    public void getCompletedTasksForUserTest() throws JSONException {
        response = this.restTemplate.getForObject("/users/auth0|5b87943afe13090f5ffd652b/completedmissions", String.class);
        JSONAssert.assertEquals("[\"Himmeli\", \"Helpperi\"]", response, false);
    }

    /* Check if creating a user is successful*/
    @Test
    public void insertUserTest() {
        User user = new User("Kettu", "Kekkuloija", 500, 1, null, 3, "Elsa1");

        ResponseEntity<User> responseEntity = restTemplate.postForEntity(
                urlWithPort("/users"), user, User.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        System.out.println(responseEntity.getBody());

        /* Check location for creating testLocation*/
        String testLocation = "/users/" + responseEntity.getBody().getAuthid();
        responseEntity = restTemplate.getForEntity(testLocation, User.class);
        assertThat(responseEntity.getStatusCodeValue(), is(200));
        User insertedUser = responseEntity.getBody();
        assertTrue(insertedUser.getUsername().equals(user.getUsername()));
    }

    @Test
    public void updateUsernameTest() throws Exception {
        User firstUser = getFirstUserFromDBTest();
        String url = urlWithPort("/users" + firstUser.getUsername() + "/username");
        ResponseEntity<User> dbresponseEntity = restTemplate.exchange(urlWithPort(url), HttpMethod.GET, null, new ParameterizedTypeReference<User>() {
        });
        User user = dbresponseEntity.getBody();
        String originalUsername = user.getUsername();
//        String originalUsername = String.valueOf(userController.getOneUserByUsername(firstUser.getUsername()));
        String updatedUsername = originalUsername + "_updated";
        firstUser.setUsername(updatedUsername);
        RequestEntity<User> requestEntity = new RequestEntity<>(firstUser, HttpMethod.PUT, new URI(url));
        ResponseEntity<User> responseEntity = restTemplate.exchange(requestEntity, User.class);
        assertEquals(updatedUsername, firstUser.getUsername());
    }

    private User getFirstUserFromDBTest() {
        ResponseEntity<List<User>> responseEntity = restTemplate.exchange(urlWithPort("/users"), HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {
        });
        List<User> allUsers = responseEntity.getBody();
//        List<User> allUsers = userController.getAllUsers();
        return allUsers.isEmpty() ? null : allUsers.get(0);
    }

    @BeforeClass
    public static void getAuthentication() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        String url = "https://jermu.eu.auth0.com/oauth/token";
        AuthenticationQuery authenticationQuery = new AuthenticationQuery("password", "hirmuinen.jermu.jermu@gmail.com",
                "Jermuilija1", "http://elsa", "etTCTSDZi6ev3eKQomKUA23YEwE0D7mw","mA1YY6aLkG2t7OsMzqjSapVBB7a1VnVJH1Zh7HuqbeUtS3mc6RPn0nQxT4mTjLJE");

        ResponseEntity<AccessToken> responseEntity = restTemplate.postForEntity(
                url, authenticationQuery, AccessToken.class);
        accessToken = responseEntity.getBody();
    }

//  static boolean lippu;
//    @Before
//    public void initusers () {
//        if(!lippu) {
//            jdbc.update("INSERT INTO USERS (ID, USERNAME, ROLE, POINTS, GROUPID, COMPLETEDMISSIONS, CONTACTPERSONUSERID) values (?, ?, ?, ?, ?, ?, ?)", 117, "Jouni", "Reagoija", 2000, 2, null, null);
//            lippu = true;
//        }
//    }
}
