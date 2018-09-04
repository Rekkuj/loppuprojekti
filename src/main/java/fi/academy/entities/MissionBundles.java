package fi.academy.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MISSIONBUNDLES")

public class MissionBundles {
    @Id
    @GeneratedValue
    private Integer bundleid;
    private Integer[] belongstogroups;
    private Integer[] listofmissions;
    private String bundlename;

    public MissionBundles(Integer bundleid, Integer[] belongstogroups, Integer[] listofmissions, String bundlename) {
        this.bundleid = bundleid;
        this.belongstogroups = belongstogroups;
        this.listofmissions = listofmissions;
        this.bundlename = bundlename;
    }

    public MissionBundles(Integer bundleid, Integer[] belongstogroups, Integer[] listofmissions) {
        this.bundleid = bundleid;
        this.belongstogroups = belongstogroups;
        this.listofmissions = listofmissions;
    }

    public MissionBundles(Integer bundleid, Integer[] listofmissions, String bundlename) {
        this.bundleid = bundleid;
        this.listofmissions = listofmissions;
        this.bundlename = bundlename;
    }

    public MissionBundles(Integer bundleid, String bundlename) {
        this.bundleid = bundleid;
        this.bundlename = bundlename;
    }

    public MissionBundles(Integer bundleid, Integer[] listofmissions) {
        this.bundleid = bundleid;
        this.listofmissions = listofmissions;
    }

    public MissionBundles() {
    }

    public Integer getBundleid() {
        return bundleid;
    }

    public void setBundleid(Integer bundleid) {
        this.bundleid = bundleid;
    }

    public Integer[] getBelongstogroups() {
        return belongstogroups;
    }

    public void setBelongstogroups(Integer[] belongstogroups) {
        this.belongstogroups = belongstogroups;
    }

    public Integer[] getListofmissions() {
        return listofmissions;
    }

    public void setListofmissions(Integer[] listofmissions) {
        this.listofmissions = listofmissions;
    }

    public String getBundlename() {
        return bundlename;
    }

    public void setBundlename(String bundlename) {
        this.bundlename = bundlename;
    }
}
