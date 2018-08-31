package fi.academy.rowmappers;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class OneStringRowMapper implements RowMapper {
    private String columnLabel;
    
    public OneStringRowMapper(String columnLabel) {
        this.columnLabel = columnLabel;
    }
    
    @Override
    public String[] mapRow(ResultSet resultSet, int i) throws SQLException {
        String[] checkIfDesiredRowIsNull;
        if (resultSet.getArray(columnLabel)==null) {
            checkIfDesiredRowIsNull = new String[0];
        } else {
            Object[] o = (Object[])resultSet.getArray(columnLabel).getArray();
            checkIfDesiredRowIsNull = Arrays.asList(o).toArray(new String[0]);
        }
        Object[] o = (Object[]) resultSet.getArray(columnLabel).getArray();
        return checkIfDesiredRowIsNull;
//        return (String[]) resultSet.getArray(columnLabel).getArray();
    }
}
