package fi.academy.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "GROUPS")
public class Group {
    @Id
    @GeneratedValue
    private Integer groupid1;
    private String groupname;
    private String teachers;
    private Integer points;
    private Integer taskscores;
    private String completedTask;
}
