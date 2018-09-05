package fi.academy.rowmappers;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class OneIntegerRowMapper implements RowMapper {
    private String columnLabel;

    public OneIntegerRowMapper(String columnLabel) {
        this.columnLabel = columnLabel;
    }
    
    @Override
    public Integer[] mapRow(ResultSet resultSet, int i) throws SQLException {
        Integer[] checkIfDesiredRowIsNull;
        if (resultSet.getArray(columnLabel)==null) {
            checkIfDesiredRowIsNull = new Integer[0];
        } else {
            Object[] o = (Object[])resultSet.getArray(columnLabel).getArray();
            checkIfDesiredRowIsNull = Arrays.asList(o).toArray(new Integer[0]);
        }
        Object[] o = (Object[]) resultSet.getArray(columnLabel).getArray();
        return checkIfDesiredRowIsNull;
    }
}
