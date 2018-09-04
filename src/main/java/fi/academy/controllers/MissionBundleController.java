package fi.academy.controllers;

import fi.academy.entities.MissionBundle;
import fi.academy.entities.User;
import fi.academy.exceptions.NotAuthorizedException;
import fi.academy.rowmappers.MissionBundleRowMapper;
import fi.academy.rowmappers.OneIntegerRowMapper;
import fi.academy.rowmappers.OneStringRowMapper;
import fi.academy.rowmappers.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/missionbundles")
public class MissionBundleController {
    private JdbcTemplate jdbc;

    public MissionBundleController(@Autowired JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @GetMapping()
    public List<MissionBundle> getAllMissionBundles(){
        List<MissionBundle> result = jdbc.query("select * from missionbundles",
                (ResultSet rs, int index) -> {
                    Integer[] ifListOfBelongsToGroupsNull;
                    if (rs.getArray("belongstogroups")==null) {
                        ifListOfBelongsToGroupsNull = new Integer[0];
                    } else {
                        Object[] obj = (Object[]) rs.getArray("belongstogroups").getArray();
                        ifListOfBelongsToGroupsNull = Arrays.asList(obj).toArray(new Integer[0]);
                    }
                    Integer[] ifListOfMissionsNull;
                    if (rs.getArray("listofmissions")==null) {
                        ifListOfMissionsNull = new Integer[0];
                    } else {
                        Object[] obj = (Object[])rs.getArray("listofmissions").getArray();
                        ifListOfMissionsNull = Arrays.asList(obj).toArray(new Integer[0]);
                    }
                    return new MissionBundle(
                            rs.getInt("bundleid"),
                            ifListOfBelongsToGroupsNull,
                            ifListOfMissionsNull,
                            rs.getString("bundlename"));
                    });
        return result;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneMissionBundleById(@PathVariable Integer id, Principal principal) {
        RowMapper<MissionBundle> missionBundleRowMapper = new MissionBundleRowMapper();
        String sql = "SELECT * FROM missionbundles WHERE bundleid=?";
        try {
            MissionBundle missionBundle = jdbc.queryForObject(sql, missionBundleRowMapper, id);
            return new ResponseEntity<MissionBundle>(missionBundle, HttpStatus.OK);
        } catch (NotAuthorizedException err) {
            throw err;
        } catch (EmptyResultDataAccessException dataAccessException) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Mission bundle not found with bundleid: " + id);
        }
    }

    @GetMapping("/{id}/belongstogroups")
    public Integer[] getBelongsToGroups(@PathVariable Integer id) {
        RowMapper<Integer[]> belongsToGroupsRowMapper = new OneIntegerRowMapper("belongstogroups");
        String sql = "SELECT belongstogroups FROM missionbundles WHERE bundleid=?";
        return jdbc.queryForObject(sql, belongsToGroupsRowMapper, id);
    }

    @GetMapping("/{id}/listofmissions")
    public Integer[] getListOfMissions(@PathVariable Integer id) {
        RowMapper<Integer[]> listOfMissionsRowMapper = new OneIntegerRowMapper("listofmissions");
        String sql = "SELECT listofmissions FROM missionbundles WHERE bundleid=?";
        return jdbc.queryForObject(sql, listOfMissionsRowMapper, id);
    }

    @PostMapping()
    public MissionBundle insertMissionBundle(@RequestBody MissionBundle missionBundle) {
        KeyHolder kh = new GeneratedKeyHolder();
        String sql = "INSERT INTO missionbundles (belongstogroups, listofmissions, bundlename) values (?, ?, ?)";

        PreparedStatementCreator preparedStatementCreator = connection -> {
            PreparedStatement preparedStatement = connection
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setArray(1, connection.createArrayOf("int",missionBundle.getBelongstogroups()));
            preparedStatement.setArray(2, connection.createArrayOf("int",missionBundle.getListofmissions()));
            preparedStatement.setString(3, missionBundle.getBundlename());
            return preparedStatement;
        };

        jdbc.update(preparedStatementCreator, kh);
        Integer generatedId = (Integer)kh.getKeys().get("bundleid");
        missionBundle.setBundleid(generatedId);
        return missionBundle;
    }

    @PutMapping("/{id}/bundlename")
    public int updateMissionBundleName(@PathVariable Integer id, @RequestBody MissionBundle missionBundle) {
        String sql = "UPDATE missionbundles SET bundlename = ? WHERE bundleid=?";
        return jdbc.update(sql, new Object[]{missionBundle.getBundlename(), id});
    }

    @PutMapping("/{id}/changebelongstogroups")
    public String updateBelongsToGroups(@PathVariable Integer id, @RequestBody MissionBundle missionBundle) {
        KeyHolder kh = new GeneratedKeyHolder();
        String sql = "UPDATE missionbundles SET belongstogroups = ? WHERE bundleid=?";

        ArrayList<Integer> existingBelongsToGroups = new ArrayList<>(Arrays.asList(getBelongsToGroups(id)));
        for (Integer mission : missionBundle.getBelongstogroups()) {
            existingBelongsToGroups.add(mission);
        }
        Integer[] updatedBelongsToGroups = new Integer[existingBelongsToGroups.size()];
        updatedBelongsToGroups = existingBelongsToGroups.toArray(updatedBelongsToGroups);

        Integer[] finalUpdatedBelongsToGroups = updatedBelongsToGroups;
        PreparedStatementCreator preparedStatementCreator = connection -> {
            PreparedStatement preparedStatement = connection
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setArray(1, connection.createArrayOf("int", finalUpdatedBelongsToGroups));
            preparedStatement.setInt(2, id);
            return preparedStatement;
        };

        jdbc.update(preparedStatementCreator, kh);
        return kh.getKeys().toString();
    }

    @PutMapping("/{id}/changelistofmissions")
    public String updateListOfMissions(@PathVariable Integer id, @RequestBody MissionBundle missionBundle) {
        KeyHolder kh = new GeneratedKeyHolder();
        String sql = "UPDATE missionbundles SET listofmissions = ? WHERE bundleid=?";

        ArrayList<Integer> existingListOfMissions = new ArrayList<>(Arrays.asList(getListOfMissions(id)));
        for (Integer mission : missionBundle.getListofmissions()) {
            existingListOfMissions.add(mission);
        }
        Integer[] updatedListOfMissions = new Integer[existingListOfMissions.size()];
        updatedListOfMissions = existingListOfMissions.toArray(updatedListOfMissions);

        Integer[] finalUpdatedListOfMissions = updatedListOfMissions;
        PreparedStatementCreator preparedStatementCreator = connection -> {
            PreparedStatement preparedStatement = connection
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setArray(1, connection.createArrayOf("int", finalUpdatedListOfMissions));
            preparedStatement.setInt(2, id);
            return preparedStatement;
        };

        jdbc.update(preparedStatementCreator, kh);
        return kh.getKeys().toString();
    }
}
