package fi.academy.rowmappers;

import fi.academy.entities.MissionBundle;

import java.sql.ResultSet;

import  org.springframework.jdbc.core.RowMapper;

import java.sql.SQLException;
import java.util.Arrays;

public class MissionBundleRowMapper implements RowMapper<MissionBundle> {

    @Override
    public MissionBundle mapRow (ResultSet missionBundleRow, int id) throws SQLException {
        Integer[] ifListOfMissionsNull;
        if (missionBundleRow.getArray("listofmissions")==null) {
            ifListOfMissionsNull = new Integer[0];
        } else {
            Object[] obj = (Object[])missionBundleRow.getArray("listofmissions").getArray();
            ifListOfMissionsNull = Arrays.asList(obj).toArray(new Integer[0]);
        }
        Integer[] ifListOfBelongsToGroupsNull;
        if (missionBundleRow.getArray("belongstogroups")==null) {
            ifListOfBelongsToGroupsNull = new Integer[0];
        } else {
            Object[] obj = (Object[])missionBundleRow.getArray("belongstogroups").getArray();
            ifListOfBelongsToGroupsNull = Arrays.asList(obj).toArray(new Integer[0]);
        }
        MissionBundle missionBundle = new MissionBundle();
        missionBundle.setBundleid(missionBundleRow.getInt("bundleid"));
        missionBundle.setBelongstogroups(ifListOfBelongsToGroupsNull);
        missionBundle.setListofmissions(ifListOfMissionsNull);
        missionBundle.setBundlename(missionBundleRow.getString("bundlename"));
        return missionBundle;
    }
}

