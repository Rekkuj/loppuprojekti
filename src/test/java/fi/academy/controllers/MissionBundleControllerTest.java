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
public class MissionBundleControllerTest {
    
    @LocalServerPort
    private int port;
    private String response;
    private String actual;
    static AccessToken accessToken;

    @Autowired
    JdbcTemplate jdbc;
    
    @Autowired
    MissionBundleController missionBundleController;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
//    @Before
//    public void beforeEachTest() {
//        this.restTemplate = restTemplate;
//        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
//        interceptors.add(new HeaderRequestInterceptor("Authorization", "Bearer " + accessToken.getAccess_token()));
//
//        restTemplate.getRestTemplate().setInterceptors(interceptors);
//    }
    
    HttpHeaders headers = new HttpHeaders();
    
    private String urlWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
    
    /*Check that missionbundles are found*/
    @Test
    public void getJermujututMissionBundle() throws JSONException {
        response = this.restTemplate.getForObject("/missionbundles", String.class);
        JSONAssert.assertEquals("[{\n" +
                "        \"bundleid\": 1,\n" +
                "        \"listofmissions\": [\n" +
                "            25,\n" +
                "            3\n" +
                "        ],\n" +
                "        \"bundlename\": \"Kettujutut\"\n" +
                "    },    {\n" +
                "        \"bundleid\": 2,\n" +
                "        \"listofmissions\": [\n" +
                "            1,\n" +
                "            2,\n" +
                "            3\n" +
                "        ],\n" +
                "        \"bundlename\": \"Jermujutut\"\n" +
                "    }]", response, false);
    
    }
    
    /*Check that context loads and responsebody contains user "Jermu"*/
    @Test
    public void getAllMissionBundlesTest(){
        ResponseEntity<List<MissionBundle>> responseEntity = restTemplate.exchange(urlWithPort("/missionbundles"), HttpMethod.GET, null, new ParameterizedTypeReference<List<MissionBundle>>() {
        });
        List<MissionBundle> missionBundle = responseEntity.getBody();
        assertTrue(missionBundle.size()==2);
    }
    
    /* Call GET by id */
    @Test
    public void getOneMissionBundleByIdTest() throws JSONException {
        response = this.restTemplate.getForObject("/missionbundles/1", String.class);
        JSONAssert.assertEquals("{\n" +
                "        \"bundleid\": 1,\n" +
                "        \"listofmissions\": [\n" +
                "            25,\n" +
                "            3\n" +
                "        ],\n" +
                "        \"bundlename\": \"Kettujutut\"\n" +
                "    }", response, false);
    }

    /* Check if listofmissions for Jermujutut equals 1, 2 and 3 */
    @Test
    public void getListOfMissionsTest() throws JSONException {
        response = this.restTemplate.getForObject("/missionbundles/2", String.class);
        JSONAssert.assertEquals("{\n" +
                "    1,\n" +
                "    2,\n" +
                "    3\n" +
                "}", response, false);
    }
//
//    /* Check if creating a user is successful*/
//    @Test
//    public void insertUserTest() {
//        User user = new User("Kettu", "Kekkuloija", 500, 1, null, 3, "Elsa1");
//
//        ResponseEntity<User> responseEntity = restTemplate.postForEntity(
//                urlWithPort("/missionbundles"), user, User.class);
//        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
//        System.out.println(responseEntity.getBody());
//
//        /* Check location for creating testLocation*/
//        String testLocation = "/missionbundles/" + responseEntity.getBody().getAuthid();
//        responseEntity = restTemplate.getForEntity(testLocation, User.class);
//        assertThat(responseEntity.getStatusCodeValue(), is(200));
//        User insertedUser = responseEntity.getBody();
//        assertTrue(insertedUser.getUsername().equals(user.getUsername()));
//    }
//
////    @Test
////    public void updateMissionBundleNameTest() throws Exception {
////        MissionBundle firstMissionBundle = getFirstMissionBundleFromDBTest();
////        String url = urlWithPort("/missionbundles" + firstMissionBundle.getBundleid() + "/bundlename");
////        Integer originalUsername = (Integer) Integer.valueOf(missionBundleController.getOneMissionBundleById(firstMissionBundle.getBundleid()));
////        String updatedUsername = originalUsername + "_updated";
////        firstMissionBundle.setUsername(updatedUsername);
////        RequestEntity<User> requestEntity = new RequestEntity<>(firstMissionBundle, HttpMethod.PUT, new URI(url));
////        ResponseEntity<User> responseEntity = restTemplate.exchange(requestEntity, User.class);
////        assertEquals(updatedUsername, firstMissionBundle.getUsername());
////    }
//
//    private MissionBundle getFirstMissionBundleFromDBTest() {
//        List<MissionBundle> allMissionBundles = missionBundleController.getAllMissionBundles();
//        return allMissionBundles.isEmpty() ? null : allMissionBundles.get(0);
//    }
//
//    @BeforeClass
//    public static void getAuthentication() {
//        TestRestTemplate restTemplate = new TestRestTemplate();
//        String url = "https://jermu.eu.auth0.com/oauth/token";
//        AuthenticationQuery authenticationQuery = new AuthenticationQuery("password", "hirmuinen.jermu.jermu@gmail.com",
//                "Jermuilija1", "http://elsa", "etTCTSDZi6ev3eKQomKUA23YEwE0D7mw","mA1YY6aLkG2t7OsMzqjSapVBB7a1VnVJH1Zh7HuqbeUtS3mc6RPn0nQxT4mTjLJE");
//
//        ResponseEntity<AccessToken> responseEntity = restTemplate.postForEntity(
//                url, authenticationQuery, AccessToken.class);
//        accessToken = responseEntity.getBody();
//    }
    
//  static boolean lippu;
//    @Before
//    public void initusers () {
//        if(!lippu) {
//            jdbc.update("INSERT INTO USERS (ID, USERNAME, ROLE, POINTS, GROUPID, COMPLETEDMISSIONS, CONTACTPERSONUSERID) values (?, ?, ?, ?, ?, ?, ?)", 117, "Jouni", "Reagoija", 2000, 2, null, null);
//            lippu = true;
//        }
//    }
}
