package com.maxmatveev.soccer.dao;

import com.google.common.collect.ImmutableMap;
import com.maxmatveev.soccer.model.Team;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Max Matveev on 09/06/15.
 */
@Component
public class TeamMapper implements RowMapper<Team> {

    public static final String NAME = "name";
    public static final String ID = "id";
    public static final String LEAGUE_ID = "league_id";

    @Override
    public Team mapRow(ResultSet rs, int rowNum) throws SQLException {
        Team team = new Team();
        team.setName(rs.getString(NAME));
        team.setId(rs.getLong(ID));
        team.setLeagueId(rs.getLong(LEAGUE_ID));
        return team;
    }

    public Map<String, Object> mapTeam(final Team team) {
        return ImmutableMap.<String, Object>of(NAME, team.getName(),
                ID, team.getId(),
                LEAGUE_ID, team.getLeagueId());
    }
}
