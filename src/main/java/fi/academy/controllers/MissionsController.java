package fi.academy.controllers;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import fi.academy.entities.Missions;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;


@RestController
@RequestMapping("/missions")
public class MissionsController {
    private JdbcTemplate jdbc;

    public MissionsController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @GetMapping()
    public List<Missions> getAllTasks() {
        List<Missions> result = jdbc.query("select * from missions",
                (ResultSet rs, int index) -> {
                    return new Missions(
                            rs.getInt("id"),
                            rs.getString("missionname"),
                            rs.getString("componentname")
                    );}
                    );
        System.out.println("tämä toimii?" + result);
        return result;
    }


    @PostMapping()
    public String insertMission (@RequestBody Missions missions) {
        KeyHolder kh = new GeneratedKeyHolder();
        String sql = "INSERT INTO missions (missionname, componentname) values (?,?)";
        PreparedStatementCreator preparedStatementCreator = connection -> {
            PreparedStatement preparedStatement = connection
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,missions.getMissionname());
            preparedStatement.setString(2, missions.getComponentname());
            return preparedStatement;
        };
        jdbc.update(preparedStatementCreator, kh);
        return kh.getKeys().toString();
    }
}
