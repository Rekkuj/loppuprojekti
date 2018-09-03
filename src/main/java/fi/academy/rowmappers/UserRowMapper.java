package fi.academy.rowmappers;

import fi.academy.entities.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class UserRowMapper implements RowMapper<User> {
    
    @Override
    public User mapRow(ResultSet userRow, int id) throws SQLException {
        String[] ifCompletedmissionNull;
        if (userRow.getArray("completedmissions")==null) {
            ifCompletedmissionNull = new String[0];
        } else {
            Object[] o = (Object[])userRow.getArray("completedmissions").getArray();
            ifCompletedmissionNull = Arrays.asList(o).toArray(new String[0]);
        }
        User user = new User();
        user.setId(userRow.getInt("id"));
        user.setUsername(userRow.getString("username"));
        user.setRole(userRow.getString("role"));
        user.setPoints(userRow.getInt("points"));
        user.setGroupid(userRow.getInt("groupId"));
        user.setCompletedmissions(ifCompletedmissionNull);
        user.setContactpersonuserid(userRow.getInt("contactpersonuserid"));
        user.setAuthid(userRow.getString("authid"));
        return user;
    }
}
