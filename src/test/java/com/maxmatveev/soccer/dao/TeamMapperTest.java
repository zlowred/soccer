package com.maxmatveev.soccer.dao;

import com.maxmatveev.soccer.model.League;
import com.maxmatveev.soccer.model.Team;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class TeamMapperTest {
    @Test
    public void testMapRs() throws SQLException {
        ResultSet rs = Mockito.mock(ResultSet.class);
        when(rs.getString("name")).thenReturn("someName");
        when(rs.getLong("id")).thenReturn(2L);
        when(rs.getLong("league_id")).thenReturn(5L);
        Team t = new TeamMapper().mapRow(rs, 0);
        assertEquals("Name should match", "someName", t.getName());
        assertEquals("ID should match", 2L, t.getId());
        assertEquals("League ID should match", 5L, t.getLeagueId());
    }

    @Test
    public void testMapTeam() {
        Team t = new Team("someName", 5L);
        t.setId(2L);
        Map<String, Object> params = new TeamMapper().mapTeam(t);
        assertEquals("Name should match", "someName", params.get("name"));
        assertEquals("ID should match", 2L, params.get("id"));
        assertEquals("League ID should match", 5L, params.get("league_id"));

    }
}