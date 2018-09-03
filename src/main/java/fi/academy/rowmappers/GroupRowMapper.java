package fi.academy.rowmappers;

import fi.academy.entities.Group;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class GroupRowMapper implements RowMapper<Group> {

    @Override
    public Group mapRow(ResultSet groupRow, int id) throws SQLException {
        String[] ifTeachersNull;
        if (groupRow.getArray("teachers")==null){
            ifTeachersNull = new String[0];
        } else {
            Object[] o = (Object[])groupRow.getArray("teachers").getArray();
            ifTeachersNull = Arrays.asList(o).toArray(new String[0]);
        }
        String[] ifPupilsNull;
        if (groupRow.getArray("pupils")==null) {
            ifPupilsNull = new String[0];
        } else {
            Object[] o = (Object[])groupRow.getArray("pupils").getArray();
            ifPupilsNull = Arrays.asList(o).toArray(new String[0]);
        }
        Group group = new Group();
        group.setGroupid(groupRow.getInt("groupid"));
        group.setGroupname(groupRow.getString("groupname"));
        group.setTeachers(ifTeachersNull);
        group.setPupils(ifPupilsNull);
        group.setTaskscores(groupRow.getInt("taskscores"));
        return group;
    }
}
