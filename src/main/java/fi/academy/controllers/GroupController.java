package fi.academy.controllers;

import fi.academy.entities.Group;
import fi.academy.entities.User;
import fi.academy.exceptions.NotAuthorizedException;
import fi.academy.rowmappers.GroupRowMapper;
import fi.academy.rowmappers.OneStringRowMapper;
import fi.academy.rowmappers.UserRowMapper;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/groups")
public class GroupController {
    private JdbcTemplate jdbc;

    public GroupController(@Autowired JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @GetMapping()
    public List<Group> getAllGroups(Principal principal) {
        RowMapper<User> userRowMapper = new UserRowMapper();
        String subQuery = "SELECT * FROM users where authid=?";
        User authenticatedPrincipal = jdbc.queryForObject(subQuery, userRowMapper, principal.getName());
        if (authenticatedPrincipal.getRole().equals("Teacher")) {
            List<Group> result = jdbc.query("select * from groups",
                    (ResultSet rs, int index) -> {
                        String[] ifTeachersNull;
                        if (rs.getArray("teachers") == null) {
                            ifTeachersNull = new String[0];
                        } else {
//                    ifTeachersNull = (String[])rs.getArray("teachers").getArray();
                            Object[] o = (Object[]) rs.getArray("teachers").getArray();
                            ifTeachersNull = Arrays.asList(o).toArray(new String[0]);
                        }
                        String[] ifPupilsNull;
                        if (rs.getArray("pupils") == null) {
                            ifPupilsNull = new String[0];
                        } else {
//                    ifPupilsNull =(String[])rs.getArray("pupils").getArray();
                            Object[] o = (Object[]) rs.getArray("pupils").getArray();
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
        } else {
            throw new NotAuthorizedException("Not authorized");
        }
    }

    @GetMapping("/{groupid}")
    public Group getOneUserById(@PathVariable Integer groupid, Principal principal) {
        RowMapper<User> userRowMapper = new UserRowMapper();
        String subQuery = "SELECT * FROM users where authid=?";
        User authenticatedPrincipal = jdbc.queryForObject(subQuery, userRowMapper, principal.getName());
        if (authenticatedPrincipal.getGroupid() == groupid || authenticatedPrincipal.getRole().equals("Teacher")) {
            RowMapper<Group> groupRowMapper = new GroupRowMapper();
            String sql = "SELECT * FROM groups WHERE groupid=?";
            return jdbc.queryForObject(sql, groupRowMapper, groupid);
        } else {
            throw new NotAuthorizedException("Not authorized");
        }
    }

    @GetMapping("/{groupid}/teachers")
    public String[] getTeachersByGroupId(@PathVariable Integer groupid, Principal principal) {
        RowMapper<User> userRowMapper = new UserRowMapper();
        String subQuery = "SELECT * FROM users where authid=?";
        User authenticatedPrincipal = jdbc.queryForObject(subQuery, userRowMapper, principal.getName());
        if (authenticatedPrincipal.getGroupid() == groupid || authenticatedPrincipal.getRole().equals("Teacher")) {
            RowMapper<String[]> teachersRowMapper = new OneStringRowMapper("teachers");
            String sql = "SELECT teachers FROM groups WHERE groupid=?";
            return jdbc.queryForObject(sql, teachersRowMapper, groupid);
        } else {
            throw new NotAuthorizedException("Not authorized");
        }
    }

    @GetMapping("/{groupid}/pupils")
    public String[] getPupilsByGroupId(@PathVariable Integer groupid, Principal principal) {
        RowMapper<User> userRowMapper = new UserRowMapper();
        String subQuery = "SELECT * FROM users where authid=?";
        User authenticatedPrincipal = jdbc.queryForObject(subQuery, userRowMapper, principal.getName());
        if (authenticatedPrincipal.getGroupid() == groupid || authenticatedPrincipal.getRole().equals("Teacher")) {
            RowMapper<String[]> pupilsRowMapper = new OneStringRowMapper("pupils");
            String sql = "SELECT pupils FROM groups WHERE groupid=?";
            return jdbc.queryForObject(sql, pupilsRowMapper, groupid);
        } else {
            throw new NotAuthorizedException("Not authorized");
        }
    }

    @GetMapping("/{groupname}/groupname")
    public Group getOneGroupByGroupname(@PathVariable String groupname, Principal principal) {
        RowMapper<User> userRowMapper = new UserRowMapper();
        String subQuery = "SELECT * FROM users where authid=?";
        User authenticatedPrincipal = jdbc.queryForObject(subQuery, userRowMapper, principal.getName());
        RowMapper<Group> groupRowMapper = new GroupRowMapper();
        String sql = "SELECT * FROM groups WHERE groupname=?";
        Group group = jdbc.queryForObject(sql, groupRowMapper, groupname);
        if (authenticatedPrincipal.getGroupid() == group.getGroupid() || authenticatedPrincipal.getRole().equals("Teacher")) {
            return group;
        } else {
            throw new NotAuthorizedException("Not authorized");
        }
    }

    @PostMapping()
    public Group insertGroup(@RequestBody Group group, Principal principal) {
        RowMapper<User> userRowMapper = new UserRowMapper();
        String subQuery = "SELECT * FROM users where authid=?";
        User authenticatedPrincipal = jdbc.queryForObject(subQuery, userRowMapper, principal.getName());
        if (authenticatedPrincipal.getRole().equals("Teacher")) {
            KeyHolder kh = new GeneratedKeyHolder();

            String sql = "INSERT INTO groups (groupid, groupname, teachers, pupils, missionscores) values (?, ?, ?, ?, ?)";

            PreparedStatementCreator preparedStatementCreator = connection -> {
                PreparedStatement preparedStatement = connection
                        .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setInt(1, group.getGroupid());
                preparedStatement.setString(2, group.getGroupname());
                preparedStatement.setArray(3, connection.createArrayOf("text", group.getTeachers()));
                preparedStatement.setArray(4, connection.createArrayOf("text", group.getPupils()));
                preparedStatement.setInt(5, 0);
                return preparedStatement;
            };

            jdbc.update(preparedStatementCreator, kh);
            Integer generatedId = (Integer) kh.getKeys().get("groupid");
            group.setGroupid(generatedId);
            return group;
        } else {
            throw new NotAuthorizedException("Not authorized");
        }
    }

    @PutMapping("/{groupid}/teachers")
    public String updateGroupTeachers(@PathVariable Integer groupid, @RequestBody Group group, Principal principal) {
        RowMapper<User> userRowMapper = new UserRowMapper();
        String subQuery = "SELECT * FROM users where authid=?";
        User authenticatedPrincipal = jdbc.queryForObject(subQuery, userRowMapper, principal.getName());
        if (authenticatedPrincipal.getRole().equals("Teacher")) {

            KeyHolder kh = new GeneratedKeyHolder();
            String sql = "UPDATE groups SET teachers = ? WHERE groupid=?";

            ArrayList<String> existingTeachers = new ArrayList<>(Arrays.asList(getTeachersByGroupId(groupid,principal)));
            for (String teacher : group.getTeachers()) {
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
        } else {
            throw new NotAuthorizedException("Not authorized");
        }
    }

    @PutMapping("/{groupid}/teachertodelete")
    public String deleteOneTeacherFromGroupTeachers(@PathVariable Integer groupid, @RequestBody String teacherToDelete,Principal principal) {
        RowMapper<User> userRowMapper = new UserRowMapper();
        String subQuery = "SELECT * FROM users where authid=?";
        User authenticatedPrincipal = jdbc.queryForObject(subQuery, userRowMapper, principal.getName());
        if (authenticatedPrincipal.getRole().equals("Teacher")) {

            KeyHolder kh = new GeneratedKeyHolder();
            String sql = "UPDATE groups SET teachers = ? WHERE groupid=?";

            ArrayList<String> existingTeachers = new ArrayList<>(Arrays.asList(getTeachersByGroupId(groupid, principal)));

            for (String teacher : existingTeachers) {
                if (teacher.equals(teacherToDelete)) {
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
        } else {
            throw new NotAuthorizedException("Not authorized");
        }
    }

    @PutMapping("/{groupid}/pupils")
    public String updateGroupPupils(@PathVariable Integer groupid, @RequestBody Group group, Principal principal) {
        RowMapper<User> userRowMapper = new UserRowMapper();
        String subQuery = "SELECT * FROM users where authid=?";
        User authenticatedPrincipal = jdbc.queryForObject(subQuery, userRowMapper, principal.getName());
        if (authenticatedPrincipal.getRole().equals("Teacher")) {

            KeyHolder kh = new GeneratedKeyHolder();
            String sql = "UPDATE groups SET pupils = ? WHERE groupid=?";

            ArrayList<String> existingPupils = new ArrayList<>(Arrays.asList(getPupilsByGroupId(groupid,principal)));
            for (String pupil : group.getPupils()) {
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
        } else {
            throw new NotAuthorizedException("Not authorized");
        }
    }

    @PutMapping("/{groupid}/pupiltodelete")
    public String deleteOnePupilFromGroupPupils(@PathVariable Integer groupid, @RequestBody String pupilToDelete,Principal principal) {
        RowMapper<User> userRowMapper = new UserRowMapper();
        String subQuery = "SELECT * FROM users where authid=?";
        User authenticatedPrincipal = jdbc.queryForObject(subQuery, userRowMapper, principal.getName());
        if (authenticatedPrincipal.getRole().equals("Teacher")) {

            KeyHolder kh = new GeneratedKeyHolder();
            String sql = "UPDATE groups SET pupils = ? WHERE groupid=?";

            ArrayList<String> existingPupils = new ArrayList<>(Arrays.asList(getPupilsByGroupId(groupid,principal)));
            for (String pupil : existingPupils) {
                if (pupil.equals(pupilToDelete)) {
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
        } else {
            throw new NotAuthorizedException("Not authorized");
        }
    }
    //TODO: tämä jää toistaiseksi tänne
//    @PutMapping("/{groupid}/scores")
//    public int updateGroupTaskscores(@PathVariable Integer groupid, @RequestBody Group group) {
//        String sql = "UPDATE groups SET missionscores = ? WHERE groupid=?";
//        return jdbc.update(sql, new Object[]{group.getMissionscores(), groupid});
//    }
}
