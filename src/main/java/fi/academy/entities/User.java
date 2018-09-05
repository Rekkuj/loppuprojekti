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
    private String[] completedmissions;
    private Integer contactpersonuserid;
    //MIIKA TESTAA
    private String authid;

    public User(int id, String username, String role, Integer points, Integer groupId, String[] completedmissions, Integer contactpersonuserid, String authid) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.points = points;
        this.groupid = groupId;
        this.completedmissions = completedmissions;
        this.contactpersonuserid = contactpersonuserid;
        this.authid = authid;
    }

    public String getAuthid() {
        return authid;
    }

    public void setAuthid(String authid) {
        this.authid = authid;
    }

    public User() {
    }
    
    public User(String username, String role, Integer points, Integer groupid, String[] completedmissions, Integer contactpersonuserid, String authid) {
        this.username = username;
        this.role = role;
        this.points = points;
        this.groupid = groupid;
        this.completedmissions = completedmissions;
        this.contactpersonuserid = contactpersonuserid;
    }
    
    public User(Integer id, String username, String role, Integer points, Integer groupid, String[] completedmissions) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.points = points;
        this.groupid = groupid;
        this.completedmissions = completedmissions !=null ? completedmissions : new String[0];
    }
    
    public User(int id, String username, String role, int points, int groupid) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.points = points;
        this.groupid = groupid;
    }
    
    public User(String username, String role, int points, int groupid, String[] completedmissions, int contactpersonuserid, String authid) {
        this.username = username;
        this.role = role;
        this.points = points;
        this.groupid = groupid;
        this.completedmissions = completedmissions ==null ? new String[0] : completedmissions;
        this.contactpersonuserid = contactpersonuserid;
        this.authid = authid;
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
    
    public String[] getCompletedmissions() {
        return completedmissions;
    }
    
    public void setCompletedmissions(String[] completedtask) {
        this.completedmissions = completedtask;
    }
    
    public Integer getContactpersonuserid() {
        return contactpersonuserid;
    }
    
    public void setContactpersonuserid(Integer contactpersonuserid) {
        this.contactpersonuserid = contactpersonuserid;
    }
}