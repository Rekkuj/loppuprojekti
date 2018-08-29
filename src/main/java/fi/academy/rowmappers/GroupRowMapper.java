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
        group.setTeachers((String[])groupRow.getArray("teachers").getArray());
        group.setPupils((String[])groupRow.getArray("pupils").getArray());
        group.setTaskscores(groupRow.getInt("taskscores"));
        return group;
    }
}
