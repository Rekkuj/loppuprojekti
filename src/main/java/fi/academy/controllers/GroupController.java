package fi.academy.controllers;

import fi.academy.entities.Group;
import fi.academy.rowmappers.GroupRowMapper;
import fi.academy.rowmappers.OneStringRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {
    private JdbcTemplate jdbc;
    
    public GroupController(@Autowired JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }
    
    @GetMapping()
    public List<Group> getAllGroups() {
        List<Group> result = jdbc.query("select * from groups",
                (ResultSet rs, int index) -> {
                String[] ifTeachersNull;
                if (rs.getArray("teachers")==null){
                    ifTeachersNull = new String[0];
                } else {
//                    ifTeachersNull = (String[])rs.getArray("teachers").getArray();
                    Object[] o = (Object[])rs.getArray("teachers").getArray();
                    ifTeachersNull = Arrays.asList(o).toArray(new String[0]);
                }
                String[] ifPupilsNull;
                if (rs.getArray("pupils")==null) {
                    ifPupilsNull = new String[0];
                } else {
//                    ifPupilsNull =(String[])rs.getArray("pupils").getArray();
                    Object[] o = (Object[])rs.getArray("pupils").getArray();
                    ifPupilsNull = Arrays.asList(o).toArray(new String[0]);
                }
                    return new Group(
                            rs.getInt("groupid"),
                            rs.getString("groupname"),
                            ifTeachersNull,
                            ifPupilsNull,
                            rs.getInt("missionscores"));
                });
        return result;
    }
    
    @GetMapping("/{groupid}")
    public Group getOneUserById(@PathVariable Integer groupid) {
        RowMapper<Group> groupRowMapper = new GroupRowMapper();
        String sql = "SELECT * FROM groups WHERE groupid=?";
        return jdbc.queryForObject(sql, groupRowMapper, groupid);
    }

    @GetMapping("/{groupid}/teachers")
    public String[] getTeachersByGroupId(@PathVariable Integer groupid) {
        RowMapper<String[]> teachersRowMapper = new OneStringRowMapper("teachers");
        String sql = "SELECT teachers FROM groups WHERE groupid=?";
        return jdbc.queryForObject(sql, teachersRowMapper, groupid);
    }
    
    @GetMapping("/{groupid}/pupils")
    public String[] getPupilsByGroupId(@PathVariable Integer groupid) {
        RowMapper<String[]> pupilsRowMapper = new OneStringRowMapper("pupils");
        String sql = "SELECT pupils FROM groups WHERE groupid=?";
        return jdbc.queryForObject(sql, pupilsRowMapper, groupid);
    }
    
    @GetMapping("/{groupname}/groupname")
    public Group getOneGroupByGroupname(@PathVariable String groupname) {
        RowMapper<Group> groupRowMapper = new GroupRowMapper();
        String sql = "SELECT * FROM groups WHERE groupname=?";
        return jdbc.queryForObject(sql, groupRowMapper, groupname);
    }

    @PostMapping()
    public String insertGroup(@RequestBody Group group) {
        KeyHolder kh = new GeneratedKeyHolder();
        String sql = "INSERT INTO groups (groupname, teachers, pupils, missioncores) values (?, ?, ?, ?)";

        PreparedStatementCreator preparedStatementCreator = connection -> {
            PreparedStatement preparedStatement = connection
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, group.getGroupname());
            preparedStatement.setArray(2, connection.createArrayOf("text", group.getTeachers()));
            preparedStatement.setArray(3, connection.createArrayOf("text", group.getPupils()));
            preparedStatement.setInt(4, 0);
            return preparedStatement;
        };

        jdbc.update(preparedStatementCreator, kh);
        return kh.getKeys().toString();
    }
    
    @PutMapping("/{groupid}/teachers")
    public String updateGroupTeachers(@PathVariable Integer groupid, @RequestBody Group group) {
        KeyHolder kh = new GeneratedKeyHolder();
        String sql = "UPDATE groups SET teachers = ? WHERE groupid=?";
    
        ArrayList<String> existingTeachers = new ArrayList<>(Arrays.asList(getTeachersByGroupId(groupid)));
        for (String teacher: group.getTeachers()) {
            existingTeachers.add(teacher);
        }
    
        String[] updatedTeachers = new String[existingTeachers.size()];
        updatedTeachers = existingTeachers.toArray(updatedTeachers);
    
        String[] finalUpdatedTeachers = updatedTeachers;
        PreparedStatementCreator preparedStatementCreator = connection -> {
            PreparedStatement preparedStatement = connection
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setArray(1, connection.createArrayOf("text", finalUpdatedTeachers));
            preparedStatement.setInt(2, groupid);
            return preparedStatement;
        };
    
        jdbc.update(preparedStatementCreator, kh);
        return kh.getKeys().toString();
    }
    
    @PutMapping("/{groupid}/teachertodelete")
    public String deleteOneTeacherFromGroupTeachers(@PathVariable Integer groupid, @RequestBody String teacherToDelete) {
        KeyHolder kh = new GeneratedKeyHolder();
        String sql = "UPDATE groups SET teachers = ? WHERE groupid=?";
        
        ArrayList<String> existingTeachers = new ArrayList<>(Arrays.asList(getTeachersByGroupId(groupid)));

        for (String teacher: existingTeachers) {
            if (teacher.equals(teacherToDelete)){
                existingTeachers.remove(teacher);
                break;
            }
        }
    
        String[] updatedTeachers = new String[existingTeachers.size()];
        updatedTeachers = existingTeachers.toArray(updatedTeachers);
    
        String[] finalUpdatedTeachers = updatedTeachers;
        PreparedStatementCreator preparedStatementCreator = connection -> {
            PreparedStatement preparedStatement = connection
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setArray(1, connection.createArrayOf("text", finalUpdatedTeachers));
            preparedStatement.setInt(2, groupid);
            return preparedStatement;
        };
        
        jdbc.update(preparedStatementCreator, kh);
        return kh.getKeys().toString();
    }
    
    @PutMapping("/{groupid}/pupils")
    public String updateGroupPupils(@PathVariable Integer groupid, @RequestBody Group group) {
    KeyHolder kh = new GeneratedKeyHolder();
    String sql = "UPDATE groups SET pupils = ? WHERE groupid=?";
    
    ArrayList<String> existingPupils = new ArrayList<>(Arrays.asList(getPupilsByGroupId(groupid)));
        for (String pupil: group.getPupils()) {
        existingPupils.add(pupil);
    }
    
    String[] updatedPupils = new String[existingPupils.size()];
    updatedPupils = existingPupils.toArray(updatedPupils);
    
    String[] finalUpdatedPupils = updatedPupils;
    PreparedStatementCreator preparedStatementCreator = connection -> {
        PreparedStatement preparedStatement = connection
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setArray(1, connection.createArrayOf("text", finalUpdatedPupils));
        preparedStatement.setInt(2, groupid);
        return preparedStatement;
    };
    
        jdbc.update(preparedStatementCreator, kh);
        return kh.getKeys().toString();
    }
    
    @PutMapping("/{groupid}/pupiltodelete")
    public String deleteOnePupilFromGroupPupils(@PathVariable Integer groupid, @RequestBody String pupilToDelete) {
        KeyHolder kh = new GeneratedKeyHolder();
        String sql = "UPDATE groups SET pupils = ? WHERE groupid=?";
        
        ArrayList<String> existingPupils = new ArrayList<>(Arrays.asList(getPupilsByGroupId(groupid)));
        for (String pupil: existingPupils) {
            if (pupil.equals(pupilToDelete)){
                existingPupils.remove(pupil);
                break;
            }
        }
        
        String[] updatedPupils = new String[existingPupils.size()];
        updatedPupils = existingPupils.toArray(updatedPupils);
        
        String[] finalUpdatedPupils = updatedPupils;
        PreparedStatementCreator preparedStatementCreator = connection -> {
            PreparedStatement preparedStatement = connection
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setArray(1, connection.createArrayOf("text", finalUpdatedPupils));
            preparedStatement.setInt(2, groupid);
            return preparedStatement;
        };
        
        jdbc.update(preparedStatementCreator, kh);
        return kh.getKeys().toString();
    }
    //TODO: tämä jää toistaiseksi tänne
//    @PutMapping("/{groupid}/scores")
//    public int updateGroupTaskscores(@PathVariable Integer groupid, @RequestBody Group group) {
//        String sql = "UPDATE groups SET missionscores = ? WHERE groupid=?";
//        return jdbc.update(sql, new Object[]{group.getTaskscores(), groupid});
//    }
}
