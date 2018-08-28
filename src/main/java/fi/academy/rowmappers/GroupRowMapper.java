package fi.academy.rowmappers;

import fi.academy.entities.Group;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupRowMapper implements RowMapper<Group> {
    
    @Override
    public Group mapRow(ResultSet groupRow, int id) throws SQLException {
        Group group = new Group();
        group.setGroupid(groupRow.getInt("groupid"));
        group.setGroupname(groupRow.getString("username"));
        group.setTeachers(groupRow.getString("teachers"));
        group.setPupils(groupRow.getString("pupils"));
        group.setTaskscores(groupRow.getInt("taskscores"));
        return group;
    }
}
