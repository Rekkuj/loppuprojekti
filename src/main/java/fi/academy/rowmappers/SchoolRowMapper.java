package fi.academy.rowmappers;
// Author: Reija Jokinen

import fi.academy.entities.School;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SchoolRowMapper implements RowMapper<School> {
    
    @Override
    public School mapRow(ResultSet schoolRow, int id) throws SQLException {
        School school = new School();
        school.setSchoolid(schoolRow.getInt("schoolid"));
        school.setSchoolname(schoolRow.getString("username"));
        school.setCity(schoolRow.getString("city"));
        school.setAddress(schoolRow.getString("address"));
        school.setContactperson(schoolRow.getString("contactperson"));
        return school;
    }
}
