package com.maxmatveev.soccer.dao;

import com.google.common.collect.ImmutableMap;
import com.maxmatveev.soccer.model.League;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Max Matveev on 09/06/15.
 */
@Component
public class LeagueMapper implements RowMapper<League> {

    public static final String NAME = "name";
    public static final String ID = "id";

    @Override
    public League mapRow(ResultSet rs, int rowNum) throws SQLException {
        League league = new League();
        league.setName(rs.getString(NAME));
        league.setId(rs.getLong(ID));
        return league;
    }

    public Map<String, Object> mapLeague(final League league) {
        return ImmutableMap.<String, Object>of(NAME, league.getName(),
                ID, league.getId());
    }
}
