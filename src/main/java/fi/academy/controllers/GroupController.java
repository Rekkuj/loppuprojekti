package fi.academy.controllers;

import fi.academy.entities.Group;
import fi.academy.rowmappers.GroupRowMapper;
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
import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {
    private JdbcTemplate jdbc;
    
    public GroupController(@Autowired JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }
    
    @GetMapping()
    public List<Group> groups() {
        List<Group> result = jdbc.query("select * from groups",
                (ResultSet rs, int index) -> {
                    return new Group(
                            rs.getInt("groupid"),
                            rs.getString("groupname"),
                            (String[])rs.getArray("teachers").getArray(),
                            (String[])rs.getArray("pupils").getArray(),
                            rs.getInt("taskscores"));
                });
        return result;
    }
    
    @GetMapping("/{groupid}")
    public Group getOneUserById(@PathVariable Integer groupid) {
        RowMapper<Group> groupRowMapper = new GroupRowMapper();
        String sql = "SELECT * FROM groups WHERE id=?";
        return jdbc.queryForObject(sql, groupRowMapper, groupid);
    }

    @GetMapping("/groupname/{groupname}")
    public Group getOneGroupByGroupname(@PathVariable String groupname) {
        RowMapper<Group> groupRowMapper = new GroupRowMapper();
        String sql = "SELECT * FROM groups WHERE groupname=?";
        return jdbc.queryForObject(sql, groupRowMapper, groupname);
    }

    @PostMapping()
    public String insertGroup(@RequestBody Group group) {
        KeyHolder kh = new GeneratedKeyHolder();
        String sql = "INSERT INTO groups (groupname, teachers, pupils, taskscores) values (?, ?, ?, ?)";

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
    
    @PutMapping("/{groupid}")
    public int updateGroupname(@PathVariable Integer groupid, @RequestBody Group group) {
        String sql = "UPDATE groups SET groupname = ? WHERE groupid=?";
        return jdbc.update(sql, new Object[]{group.getGroupname(), groupid});
    }
    
}
