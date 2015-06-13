package com.maxmatveev.soccer.dao;

import com.maxmatveev.soccer.model.League;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LeagueMapperTest {
    @Test
    public void testMapRs() throws SQLException {
        ResultSet rs = Mockito.mock(ResultSet.class);
        when(rs.getString("name")).thenReturn("someName");
        when(rs.getLong("id")).thenReturn(2L);
        League l = new LeagueMapper().mapRow(rs, 0);
        assertEquals("Name should match", "someName", l.getName());
        assertEquals("ID should match", 2L, l.getId());
    }

    @Test
    public void testMapLeague() {
        League l = new League("someName");
        l.setId(2L);
        Map<String, Object> params = new LeagueMapper().mapLeague(l);
        assertEquals("Name should match", "someName", params.get("name"));
        assertEquals("ID should match", 2L, params.get("id"));

    }
}