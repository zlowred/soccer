package com.maxmatveev.soccer.service;

import com.maxmatveev.soccer.dao.LeagueDao;
import com.maxmatveev.soccer.dao.PlayerDao;
import com.maxmatveev.soccer.dao.TeamDao;
import com.maxmatveev.soccer.model.League;
import com.maxmatveev.soccer.model.Player;
import com.maxmatveev.soccer.model.Team;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.mockito.Mockito.*;

public class CsvDataLoaderTest {
    @Mock
    private LeagueDao leagueDao;
    @Mock
    private TeamDao teamDao;
    @Mock
    private PlayerDao playerDao;

    private CsvDataLoader csvDataLoader;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        csvDataLoader = new CsvDataLoader();
        csvDataLoader.setLeagueDao(leagueDao);
        csvDataLoader.setPlayerDao(playerDao);
        csvDataLoader.setTeamDao(teamDao);
    }

    @Test
    public void testEmptyInputDoesNothing() throws IOException, DataLoaderException {
        csvDataLoader.loadData(new InputStreamReader(new ByteArrayInputStream("".getBytes())));
    }

    @Test(expected = DataLoaderException.class)
    public void testIncompleteHeaderFails() throws IOException, DataLoaderException {
        csvDataLoader.loadData(new InputStreamReader(new ByteArrayInputStream("#League,Team,Number,Name".getBytes())));
    }

    @Test(expected = DataLoaderException.class)
    public void testMissingHeaderFails() throws IOException, DataLoaderException {
        csvDataLoader.loadData(new InputStreamReader(new ByteArrayInputStream("English Premier League,Arsenal,13,David Ospina,G".getBytes())));
    }

    @Test
    public void testLeagueCreatedJustOnce() throws IOException, DataLoaderException {
        String testData = "#League,Team,Number,Name,Position\n" +
                "English Premier League,Arsenal,1,Wojciech Szczęsny,G\n" +
                "English Premier League,Arsenal,2,Mathieu Debuchy,D";
        when(teamDao.createTeam(any(Team.class))).thenReturn(new Team("Team", 1));
        when(leagueDao.createLeague(any(League.class))).thenReturn(new League("League"));
        csvDataLoader.loadData(new InputStreamReader(new ByteArrayInputStream(testData.getBytes())));
        verify(leagueDao, Mockito.times(1)).createLeague(Mockito.any(League.class));
    }

    @Test
    public void testTeamCreatedJustOnce() throws IOException, DataLoaderException {
        String testData = "#League,Team,Number,Name,Position\n" +
                "English Premier League,Arsenal,1,Wojciech Szczęsny,G\n" +
                "English Premier League,Arsenal,2,Mathieu Debuchy,D";
        when(teamDao.createTeam(any(Team.class))).thenReturn(new Team("Team", 1));
        when(leagueDao.createLeague(any(League.class))).thenReturn(new League("League"));
        csvDataLoader.loadData(new InputStreamReader(new ByteArrayInputStream(testData.getBytes())));
        verify(teamDao, Mockito.times(1)).createTeam(Mockito.any(Team.class));
    }

    @Test
    public void testPlayerCreatedTwice() throws IOException, DataLoaderException {
        String testData = "#League,Team,Number,Name,Position\n" +
                "English Premier League,Arsenal,1,Wojciech Szczęsny,G\n" +
                "English Premier League,Arsenal,2,Mathieu Debuchy,D";
        when(teamDao.createTeam(any(Team.class))).thenReturn(new Team("Team", 1));
        when(leagueDao.createLeague(any(League.class))).thenReturn(new League("League"));
        csvDataLoader.loadData(new InputStreamReader(new ByteArrayInputStream(testData.getBytes())));
        verify(playerDao, Mockito.times(2)).createPlayer(Mockito.any(Player.class));
    }

    @Test
    public void testMultipleLeaguesTeamsPlayersCreated() throws IOException, DataLoaderException {
        String testData = "#League,Team,Number,Name,Position\n" +
                "English Premier League,Arsenal,1,Wojciech Szczęsny,G\n" +
                "English Premier League,Arsenal,2,Mathieu Debuchy,D,\n" +
                "Argentinian Primera División,Atlético Rafaela,3,Lucas Kruspzky,D\n" +
                "Argentinian Primera División,Atlético Rafaela,4,Dimas Morales,D\n";
        when(teamDao.createTeam(any(Team.class))).thenReturn(new Team("Team", 1));
        when(leagueDao.createLeague(any(League.class))).thenReturn(new League("League"));
        csvDataLoader.loadData(new InputStreamReader(new ByteArrayInputStream(testData.getBytes())));
        verify(playerDao, Mockito.times(4)).createPlayer(Mockito.any(Player.class));
        verify(teamDao, Mockito.times(2)).createTeam(Mockito.any(Team.class));
        verify(leagueDao, Mockito.times(2)).createLeague(Mockito.any(League.class));
    }

    @Test
    public void testInvalidLinesHandled() throws IOException, DataLoaderException {
        String testData = "#League,Team,Number,Name,Position\n" +
                "English Premier League,Arsenal,1,Wojciech Szczęsny,G\n" +
                "English Premier League,Arsenal,2,Mathieu Debuchy,D,\n" +
                "English Premier League,Arsenal,2,Mathieu Debuchy,,\n" +
                "Argentinian Primera División,Atlético Rafaela,3,,\n" +
                "Argentinian Primera División,Atlético Rafaela,3,Lucas Kruspzky,D\n" +
                "Argentinian Primera División,Atlético Rafaela,4,Dimas Morales,D\n";
        when(teamDao.createTeam(any(Team.class))).thenReturn(new Team("Team", 1));
        when(leagueDao.createLeague(any(League.class))).thenReturn(new League("League"));
        csvDataLoader.loadData(new InputStreamReader(new ByteArrayInputStream(testData.getBytes())));
        verify(playerDao, Mockito.times(4)).createPlayer(Mockito.any(Player.class));
        verify(teamDao, Mockito.times(2)).createTeam(Mockito.any(Team.class));
        verify(leagueDao, Mockito.times(2)).createLeague(Mockito.any(League.class));
    }
}