package fi.academy.controllers;


import fi.academy.entities.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertTrue;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    
    @MockBean
    private JdbcTemplate jdbc;

    @Autowired
    private MockMvc mockMvc;
    
    private String response;
    private String actual;
    
    HttpHeaders headers = new HttpHeaders();
    private MockHttpServletRequestBuilder requestBuilder;
    
    @Test
    public void addUserTest() throws Exception {
        String[] somedata = {"Himmeli", "Helpperi"};
        User user = new User(10003, "ReijaJ", "Testaaja", 10, 1, somedata, 1);
    
        requestBuilder = MockMvcRequestBuilders
                .post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\n" +
                        "    \"username\": \"Juuuhhh\",\n" +
                        "    \"role\": null,\n" +
                        "    \"points\": 20,\n" +
                        "    \"groupId\": 2,\n" +
                        "    \"completedtasks\": null,\n" +
                        "    \"contactpersonuserid\": 3\n" +
                        "}");
        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }
    
    @Test
    public void getOneUserByIdTest() throws Exception {
        requestBuilder = MockMvcRequestBuilders
                .get("/users/1/id")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();
    }
    
//
//    @Test
//    public void getOneUserByUsernameTest() throws JSONException {
//        response = this.restTemplate.getForObject("/users/Rekku/username", String.class);
//        JSONAssert.assertEquals("{}", response, false);
//    }
//
//    @Test
//    public void getCompletedTasksForUserTest() throws JSONException {
//        response = this.restTemplate.getForObject("/users/1/completedtasks", String.class);
//        JSONAssert.assertEquals("{[]}", response, false);
//    }
    
}
