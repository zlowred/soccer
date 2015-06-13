package com.maxmatveev.soccer.ui.service;

import com.maxmatveev.soccer.dao.LeagueDao;
import com.maxmatveev.soccer.dao.PlayerDao;
import com.maxmatveev.soccer.dao.TeamDao;
import com.maxmatveev.soccer.model.League;
import com.maxmatveev.soccer.model.Player;
import com.maxmatveev.soccer.model.Team;
import com.maxmatveev.soccer.ui.model.DatabaseRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by Max Matveev on 09/06/15.
 */
@Component
public class DatabaseService {
    private LeagueDao leagueDao;
    private TeamDao teamDao;
    private PlayerDao playerDao;

    @Autowired
    public void setLeagueDao(LeagueDao leagueDao) {
        this.leagueDao = leagueDao;
    }

    @Autowired
    public void setTeamDao(TeamDao teamDao) {
        this.teamDao = teamDao;
    }

    @Autowired
    public void setPlayerDao(PlayerDao playerDao) {
        this.playerDao = playerDao;
    }

    @Transactional(readOnly = true)
    public List<DatabaseRecord> getAllRecords() {
        return generateRecords(playerDao.getAllPlayers());
    }

    private List<DatabaseRecord> generateRecords(Collection<Player> players) {
        Map<Long, League> leagueCache = new HashMap<>();
        Map<Long, Team> teamCache = new HashMap<>();
        List<DatabaseRecord> records = new ArrayList<>();
        players.stream().forEach(player -> {
            DatabaseRecord record = new DatabaseRecord();
            record.setId(player.getId());
            record.setNumber(player.getNumber());
            record.setFullName(player.getFullName());
            if (!teamCache.containsKey(player.getTeamId())) {
                teamCache.put(player.getTeamId(), teamDao.getTeamById(player.getTeamId()));
            }
            Team team = teamCache.get(player.getTeamId());
            record.setTeam(team.getName());
            if (!leagueCache.containsKey(team.getLeagueId())) {
                leagueCache.put(team.getLeagueId(), leagueDao.getLeagueById(team.getLeagueId()));
            }
            League league = leagueCache.get(team.getLeagueId());
            record.setLeague(league.getName());
            record.setPosition(player.getPosition().name());
            records.add(record);
        });
        return records;
    }

    @Transactional(readOnly = true)
    public Collection<League> getAllLeagues() {
        return leagueDao.getAllLeagues();
    }

    @Transactional(readOnly = true)
    public Collection<Team> getAllTeams() {
        return teamDao.getAllTeams();
    }

    @Transactional(readOnly = true)
    public Collection<Team> getTeamsInLeague(League league) {
        return teamDao.getTeamsInLeagues(league);
    }

    @Transactional(readOnly = true)
    public Collection<DatabaseRecord> getRecordsForLeague(League league) {
        return generateRecords(playerDao.getPlayersInLeagues(league));
    }

    @Transactional(readOnly = true)
    public Collection<DatabaseRecord> getRecordsForTeam(Team team) {
        return generateRecords(playerDao.getPlayersInTeams(team));
    }
}
