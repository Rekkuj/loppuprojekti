package fi.academy.entities;

public class AuthenticationQuery {
    private String grant_type;
    private String username;
    private String password;
    private String audience;
    private String client_id;
    private String client_secret;
    
    public AuthenticationQuery() {
    }
    
    public AuthenticationQuery(String grant_type, String username, String password, String audience, String client_id, String client_secret) {
        this.grant_type = grant_type;
        this.username = username;
        this.password = password;
        this.audience = audience;
        this.client_id = client_id;
        this.client_secret = client_secret;
    }
    
    public String getGrant_type() {
        return grant_type;
    }
    
    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getAudience() {
        return audience;
    }
    
    public void setAudience(String audience) {
        this.audience = audience;
    }
    
    public String getClient_id() {
        return client_id;
    }
    
    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }
    
    public String getClient_secret() {
        return client_secret;
    }
    
    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }
    //"{\n" +
//        " \"grant_type\":\"password\",\n" +
//        " \"username\": \"hirmuinen.jermu.jermu@gmail.com\",\n" +
//        " \"password\": \"Jermuilija1\",\n" +
//        " \"audience\": \"http://elsa\",\n" +
//        " \"client_id\": \"etTCTSDZi6ev3eKQomKUA23YEwE0D7mw\",\n" +
//        " \"client_secret\": \"mA1YY6aLkG2t7OsMzqjSapVBB7a1VnVJH1Zh7HuqbeUtS3mc6RPn0nQxT4mTjLJE\"\n" +
//        "}";
}
