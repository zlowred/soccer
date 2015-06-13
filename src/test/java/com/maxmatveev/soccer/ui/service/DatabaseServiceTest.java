package com.maxmatveev.soccer.ui.service;

import com.maxmatveev.soccer.dao.LeagueDao;
import com.maxmatveev.soccer.dao.PlayerDao;
import com.maxmatveev.soccer.dao.TeamDao;
import com.maxmatveev.soccer.model.League;
import com.maxmatveev.soccer.model.Player;
import com.maxmatveev.soccer.model.Team;
import com.maxmatveev.soccer.ui.model.DatabaseRecord;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DatabaseServiceTest {
    @Mock
    private LeagueDao leagueDao;
    @Mock
    private TeamDao teamDao;
    @Mock
    private PlayerDao playerDao;
    private DatabaseService databaseService;
    private Player p1;
    private Player p2;
    private Team t1;
    private Team t2;
    private League l1;
    private League l2;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        databaseService = new DatabaseService();
        databaseService.setTeamDao(teamDao);
        databaseService.setPlayerDao(playerDao);
        databaseService.setLeagueDao(leagueDao);

        p1 = new Player("p1 name", (byte) 1, Player.Position.D, 2L);
        p2 = new Player("p2 name", (byte) 2, Player.Position.F, 3L);
        t1 = new Team("t1 name", 5L);
        t2 = new Team("t2 name", 6L);
        l1 = new League("l1 name");
        l2 = new League("l2 name");
        when(playerDao.getAllPlayers()).thenReturn(Arrays.asList(p1, p2));
        when(teamDao.getTeamById(2L)).thenReturn(t1);
        when(teamDao.getTeamById(3L)).thenReturn(t2);
        when(leagueDao.getLeagueById(5L)).thenReturn(l1);
        when(leagueDao.getLeagueById(6L)).thenReturn(l2);
        when(teamDao.getAllTeams()).thenReturn(Arrays.asList(t1, t2));
        when(leagueDao.getAllLeagues()).thenReturn(Arrays.asList(l1, l2));
        when(teamDao.getTeamsInLeagues(l1)).thenReturn(Arrays.asList(t1));
        when(playerDao.getPlayersInLeagues(l1)).thenReturn(Arrays.asList(p1));
        when(playerDao.getPlayersInTeams(t2)).thenReturn(Arrays.asList(p2));
    }

    @Test
    public void testGetAllRecords() {
        List<DatabaseRecord> res = databaseService.getAllRecords();
        assertEquals(2, res.size());
        DatabaseRecord r1 = res.get(0);
        assertEquals("l1 name", r1.getLeague());
        assertEquals("t1 name", r1.getTeam());
        assertEquals("p1 name", r1.getFullName());
        assertEquals((byte) 1, r1.getNumber());
        assertEquals("D", r1.getPosition());
    }

    @Test
    public void testGetAllLeagues() {
        Collection<League> res = databaseService.getAllLeagues();
        assertEquals(2, res.size());
        League l = res.iterator().next();
        assertEquals("l1 name", l.getName());
    }

    @Test
    public void testGetAllTeams() {
        Collection<Team> res = databaseService.getAllTeams();
        assertEquals(2, res.size());
        Team t = res.iterator().next();
        assertEquals("t1 name", t.getName());
    }

    @Test
    public void testGetTeamsInLeague() {
        Collection<Team> res = databaseService.getTeamsInLeague(l1);
        assertEquals(Arrays.asList(t1), res);
    }

    @Test
    public void testGetRecordsByLeague() {
        Collection<DatabaseRecord> res = databaseService.getRecordsForLeague(l1);
        assertEquals(1, res.size());
        DatabaseRecord r1 = res.iterator().next();
        assertEquals("l1 name", r1.getLeague());
        assertEquals("t1 name", r1.getTeam());
        assertEquals("p1 name", r1.getFullName());
        assertEquals((byte) 1, r1.getNumber());
        assertEquals("D", r1.getPosition());
    }

    @Test
    public void testGetRecordsByTeam() {
        Collection<DatabaseRecord> res = databaseService.getRecordsForTeam(t2);
        assertEquals(1, res.size());
        DatabaseRecord r1 = res.iterator().next();
        assertEquals("l2 name", r1.getLeague());
        assertEquals("t2 name", r1.getTeam());
        assertEquals("p2 name", r1.getFullName());
        assertEquals((byte) 2, r1.getNumber());
        assertEquals("F", r1.getPosition());
    }
}