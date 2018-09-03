package fi.academy.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MISSIONS")

public class Missions {
    @Id
    @GeneratedValue
    private Integer id;
    private String missionname;
    private String componentname;

    public Missions(Integer id, String missionname, String componentname) {
        this.id = id;
        this.missionname = missionname;
        this.componentname = componentname;
    }

    public Missions(String missionname, String componentname) {
        this.missionname = missionname;
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

    public String getMissionname() {
        return missionname;
    }

    public void setMissionname(String missionname) {
        this.missionname = missionname;
    }

    public String getComponentname() {
        return componentname;
    }

    public void setComponentname(String componentname) {
        this.componentname = componentname;
    }
}
