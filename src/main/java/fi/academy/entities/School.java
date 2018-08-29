package fi.academy.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SCHOOLS")
public class School {
    @Id
    @GeneratedValue
    private Integer schoolid;
    private String schoolname;
    private String city;
    private String address;
    private String contactperson;
    
    public School() {
    }
    
    public School(String schoolname, String city, String address, String contactperson) {
        this.schoolname = schoolname;
        this.city = city;
        this.address = address;
        this.contactperson = contactperson;
    }
    
    public Integer getSchoolid() {
        return schoolid;
    }
    
    public void setSchoolid(Integer schoolid) {
        this.schoolid = schoolid;
    }
    
    public String getSchoolname() {
        return schoolname;
    }
    
    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getContactperson() {
        return contactperson;
    }
    
    public void setContactperson(String contactperson) {
        this.contactperson = contactperson;
    }
}
