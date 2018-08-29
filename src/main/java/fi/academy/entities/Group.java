package fi.academy.entities;
// Author: Reija Jokinen

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "GROUPS")
public class Group {
    @Id
    @GeneratedValue
    private Integer groupid;
    private String groupname;
    private String[] teachers;
    private String[] pupils;
    private Integer taskscores;
    
    public Group() {
    }
    
    public Group(Integer groupid, String groupname, String[] teachers, String[] pupils, Integer taskscores) {
        this.groupid = groupid;
        this.groupname = groupname;
        this.teachers = teachers;
        this.pupils = pupils;
        this.taskscores = taskscores;
    }
    
    public Group(int groupid, String groupname, int taskscores) {
        this.groupid = groupid;
        this.groupname = groupname;
        this.taskscores = taskscores;
    }
    
    public Integer getGroupid() {
        return groupid;
    }
    
    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }
    
    public String getGroupname() {
        return groupname;
    }
    
    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }
    
    public String[] getTeachers() {
        return teachers;
    }
    
    public void setTeachers(String[] teachers) {
        this.teachers = teachers;
    }
    
    public String[] getPupils() {
        return pupils;
    }
    
    public void setPupils(String[] pupils) {
        this.pupils = pupils;
    }
    
    public Integer getTaskscores() {
        return taskscores;
    }
    
    public void setTaskscores(Integer taskscores) {
        this.taskscores = taskscores;
    }
}
