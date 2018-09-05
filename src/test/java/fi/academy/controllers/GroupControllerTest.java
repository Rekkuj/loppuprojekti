package fi.academy.controllers;

import fi.academy.LoppuprojektiApplication;
import fi.academy.entities.*;
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
public class GroupControllerTest {
    
    @LocalServerPort
    private int port;
    private String response;
    private String actual;
    static AccessToken accessToken;

    @Autowired
    JdbcTemplate jdbc;
    
    @Autowired
    GroupController groupController;
    
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
    
    /*Check that context loads and responsebody contains group "Jermut"*/
    @Test
    public void getJermutGroup() throws JSONException{
        
        response = this.restTemplate.getForObject("/groups", String.class);
        JSONAssert.assertEquals("[{\n" +
                "        \"groupid\": 1,\n" +
                "        \"groupname\": \"Kettupoppoo\",\n" +
                "        \"missionscores\": 250\n" +
                "    },{\n" +
                "         \"groupid\": 2,\n" +
                "         \"groupname\": \"Jermut\",\n" +
                "         \"teachers\": [\"Tommi\", \"Samu\"],\n" +
                "         \"missionscores\": 20500\n" +
                "   }]", response, false);
    }
    
    /*Check that context loads and responsebody contains user "Jermu"*/
    @Test
    public void getAllGroupsTest(){
        ResponseEntity<List<Group>> responseEntity = restTemplate.exchange(urlWithPort("/groups"), HttpMethod.GET, null, new ParameterizedTypeReference<List<Group>>() {
        });
        List<Group> group = responseEntity.getBody();
        assertTrue(group.size()==2);
    }

    /* Call GET by groupname */
    @Test
    public void getOneGroupByGroupnameTest() throws JSONException {
        response = this.restTemplate.getForObject("/groups/Kettupoppoo/groupname", String.class);
        JSONAssert.assertEquals("{\n" +
                "        \"groupid\": 1,\n" +
                "        \"groupname\": \"Kettupoppoo\",\n" +
                "        \"missionscores\": 250\n" +
                "    }", response, false);
    }

    /* Call GET by groupid */
    @Test
    public void getOneGroupByGroupidTest() throws JSONException {
        response = this.restTemplate.getForObject("/groups/2", String.class);
        JSONAssert.assertEquals("{\n" +
                "         \"groupid\": 2,\n" +
                "         \"groupname\": \"Jermut\",\n" +
                "         \"teachers\": [\"Tommi\", \"Samu\"],\n" +
                "         \"missionscores\": 20500\n" +
                "   }", response, false);
    }

    /* Check if Jermu groups's teachers equals Tommi and Samu */
    @Test
    public void getCompletedTasksForUserTest() throws JSONException {
        response = this.restTemplate.getForObject("/groups/2/teachers", String.class);
        JSONAssert.assertEquals("[\"Tommi\", \"Samu\"]", response, false);
    }

    /* Check if creating a group is successful*/
    @Test
    public void insertGroupTest() {
        Group group = new Group(77,"AccentureAcademy",null, null, 50000);

        ResponseEntity<Group> responseEntity = restTemplate.postForEntity(
                urlWithPort("/groups"), group, Group.class);
//        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        System.out.println(responseEntity.getBody());

        /* Check location for creating testLocation*/
        String testLocation = "/groups/77";
        responseEntity = restTemplate.getForEntity(testLocation, Group.class);
        assertThat(responseEntity.getStatusCodeValue(), is(200));
        Group insertedGroup = responseEntity.getBody();
        assertTrue(insertedGroup.getGroupname().equals(group.getGroupname()));
    }

    @Test
    public void updateGroupnameTest() throws Exception {
        Group firstGroup = getFirstGroupFromDBTest();
        String url = urlWithPort("/groups" + firstGroup.getGroupname() + "/groupname");
        String originalGroupname = firstGroup.getGroupname();
        String updatedGroupname = originalGroupname + "_updated";
        firstGroup.setGroupname(updatedGroupname);
        RequestEntity<Group> requestEntity = new RequestEntity<>(firstGroup, HttpMethod.PUT, new URI(url));
        ResponseEntity<Group> responseEntity = restTemplate.exchange(requestEntity, Group.class);
        assertEquals(updatedGroupname, firstGroup.getGroupname());
    }

    private Group getFirstGroupFromDBTest() {
        ResponseEntity<List<Group>> responseEntity = restTemplate.exchange(urlWithPort("/groups"), HttpMethod.GET, null, new ParameterizedTypeReference<List<Group>>() {
        });
        List<Group> allGroups = responseEntity.getBody();
//        List<Group> allGroups = groupController.getAllGroups();
        return allGroups.isEmpty() ? null : allGroups.get(0);
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
}
