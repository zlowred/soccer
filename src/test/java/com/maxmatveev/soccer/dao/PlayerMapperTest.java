package com.maxmatveev.soccer.dao;

import com.maxmatveev.soccer.model.Player;
import com.maxmatveev.soccer.model.Team;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class PlayerMapperTest {
    @Test
    public void testMapRs() throws SQLException {
        ResultSet rs = Mockito.mock(ResultSet.class);
        when(rs.getString("full_name")).thenReturn("someName");
        when(rs.getLong("id")).thenReturn(2L);
        when(rs.getLong("team_id")).thenReturn(5L);
        when(rs.getByte("number")).thenReturn((byte) 1);
        when(rs.getString("position")).thenReturn("D");
        Player p = new PlayerMapper().mapRow(rs, 0);
        assertEquals("Name should match", "someName", p.getFullName());
        assertEquals("ID should match", 2L, p.getId());
        assertEquals("Team ID should match", 5L, p.getTeamId());
        assertEquals("Number should match", (byte) 1, p.getNumber());
        assertEquals("Position should match", Player.Position.D, p.getPosition());

    }

    @Test
    public void testMapPlayer() {
        Player p = new Player("someName", (byte) 1, Player.Position.D, 5L);
        p.setId(2L);
        Map<String, Object> params = new PlayerMapper().mapPlayer(p);
        assertEquals("Name should match", "someName", params.get("full_name"));
        assertEquals("ID should match", 2L, params.get("id"));
        assertEquals("Team ID should match", 5L, params.get("team_id"));
        assertEquals("Number should match", (byte) 1, params.get("number"));
        assertEquals("Position should match", "D", params.get("position"));

    }
}