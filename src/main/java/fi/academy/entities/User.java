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
    private Integer groupId;
    private String[] completedtasks;
    private Integer contactpersonuserid;
    
    public User() {
    }

    public User(Integer id, String username, String role, Integer points, Integer groupId, String[] completedtasks) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.points = points;
        this.groupId = groupId;
        this.completedtasks = completedtasks;
    }
    
    public User(int id, String username, String role, int points, int groupId) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.points = points;
        this.groupId = groupId;
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

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
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