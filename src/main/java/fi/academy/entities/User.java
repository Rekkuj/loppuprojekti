package fi.academy.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USERS")
public class User {
    @Id @GeneratedValue
    private Integer id;
    private String username;
    private String role;
    private Integer points;
    private Integer groupid;
    private String[] completedtasks;
    private Integer contactpersonuserid;
    //MIIKA TESTAA
    private String testid;

    public User(int id, String username, String role, Integer points, Integer groupId, String[] completedtasks, Integer contactpersonuserid, String testid) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.points = points;
        this.groupid = groupId;
        this.completedtasks = completedtasks;
        this.contactpersonuserid = contactpersonuserid;
        this.testid = testid;
    }

    public String getTestid() {
        return testid;

    }

    public void setTestid(String testid) {
        this.testid = testid;
    }

    public User() {
    }

    public User(Integer id, String username, String role, Integer points, Integer groupid, String[] completedtasks) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.points = points;
        this.groupid = groupid;
        this.completedtasks = completedtasks!=null ? completedtasks : new String[0];
    }
    
    public User(int id, String username, String role, int points, int groupid) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.points = points;
        this.groupid = groupid;
    }
    
    public User(int id, String username, String role, int points, int groupid, String[] completedtasks, int contactpersonuserid) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.points = points;
        this.groupid = groupid;
        this.completedtasks = completedtasks==null ? new String[0] : completedtasks;
        this.contactpersonuserid = contactpersonuserid;
    }
    
    public User(String username, String role, int points, int groupid, int contactpersonuserid) {
        this.username = username;
        this.role = role;
        this.points = points;
        this.groupid = groupid;
        this.contactpersonuserid = contactpersonuserid;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getGroupid() {
        return groupid;
    }

    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }
    
    public String[] getCompletedtasks() {
        return completedtasks;
    }
    
    public void setCompletedtasks(String[] completedtask) {
        this.completedtasks = completedtask;
    }
    
    public Integer getContactpersonuserid() {
        return contactpersonuserid;
    }
    
    public void setContactpersonuserid(Integer contactpersonuserid) {
        this.contactpersonuserid = contactpersonuserid;
    }
}