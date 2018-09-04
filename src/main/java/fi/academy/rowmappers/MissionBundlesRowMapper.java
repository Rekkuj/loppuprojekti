package fi.academy.rowmappers;

import fi.academy.entities.MissionBundles;
import fi.academy.entities.Missions;
import java.sql.ResultSet;

import  org.springframework.jdbc.core.RowMapper;

import java.sql.SQLException;
import java.util.Arrays;

public class MissionBundlesRowMapper implements RowMapper<Missions> {

    @Override
    public MissionBundles mapRo1w (ResultSet missionBundlesRow, int id) throws SQLException {
        Integer[] ifListOfMissionsNull;
        if (missionBundlesRow.getArray("listofmissions")==null) {
            ifListOfMissionsNull = new Integer[0];
        } else {
            Object[] obj = (Object[])missionBundlesRow.getArray("listofmissions").getArray();
            ifListOfMissionsNull = Arrays.asList(obj).toArray(new Integer[0]);
        }
        Integer[] ifListOfBelongsToGroupsNull;
        if (missionBundlesRow.getArray("belongstogroups")==null) {
            ifListOfBelongsToGroupsNull = new Integer[0];
        } else {
            Object[] obj = (Object[])missionBundlesRow.getArray("belongstogroups").getArray();
            ifListOfBelongsToGroupsNull = Arrays.asList(obj).toArray(new Integer[0]);
        }
        MissionBundles missionBundle = new MissionBundles();
        missionBundle.setBundleid(missionBundlesRow.getInt("bundleid"));
        missionBundle.setBelongstogroups(ifListOfBelongsToGroupsNull);
        missionBundle.setListofmissions(ifListOfMissionsNull);
        missionBundle.setBundlename(missionBundlesRow.getString("bundlename"));
        return missionBundle;
    }
}

