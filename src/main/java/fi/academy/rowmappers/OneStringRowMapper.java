package fi.academy.rowmappers;
// Author: Reija Jokinen

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OneStringRowMapper implements RowMapper {
    private String columnLabel;
    
    public OneStringRowMapper(String columnLabel) {
        this.columnLabel = columnLabel;
    }
    
    @Override
    public String[] mapRow(ResultSet resultSet, int i) throws SQLException {
        return (String[])resultSet.getArray(columnLabel).getArray();
    }
}
