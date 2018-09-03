package fi.academy.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TASKS")

public class Missions {
    @Id
    @GeneratedValue
    private Integer id;
    private String taskname;
    private String componentname;

    public Missions(Integer id, String taskname, String componentname) {
        this.id = id;
        this.taskname = taskname;
        this.componentname = componentname;
    }

    public Missions(String taskname, String componentname) {
        this.taskname = taskname;
        this.componentname = componentname;
    }

    public Missions() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public String getComponentname() {
        return componentname;
    }

    public void setComponentname(String componentname) {
        this.componentname = componentname;
    }
}
