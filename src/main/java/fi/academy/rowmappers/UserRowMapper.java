package fi.academy.rowmappers;

import fi.academy.entities.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    
    @Override
    public User mapRow(ResultSet userRow, int id) throws SQLException {
        User user = new User();
        user.setId(userRow.getInt("id"));
        user.setUsername(userRow.getString("username"));
        return user;
    }
}