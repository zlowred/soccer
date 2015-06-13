package com.maxmatveev.soccer.service;

import com.maxmatveev.soccer.dao.LeagueDao;
import com.maxmatveev.soccer.dao.PlayerDao;
import com.maxmatveev.soccer.dao.TeamDao;
import com.maxmatveev.soccer.model.League;
import com.maxmatveev.soccer.model.Player;
import com.maxmatveev.soccer.model.Team;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * Created by Max Matveev on 09/06/15.
 */
@Component
public class CsvDataLoader implements DataLoader {
    private static final Logger LOG = LoggerFactory.getLogger(CsvDataLoader.class);

    private LeagueDao leagueDao;
    private TeamDao teamDao;
    private PlayerDao playerDao;
    private Map<String, League> leagueCache;
    private Map<String, Team> teamCache;

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

    @Override
    @Transactional
    public void loadData(Reader reader) throws IOException, DataLoaderException {
        // reset caches before import
        leagueCache = new HashMap<>();
        teamCache = new HashMap<>();

        for (CSVRecord record : CSVFormat.DEFAULT.parse(reader)) {
            if (record.getRecordNumber() == 1) {
                // Check CSV header
                if (record.size() != 5) {
                    throw new DataLoaderException("CSV file should have exactly 5 columns");
                }
                List<String> header = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    header.add(record.get(i));
                }
                if (!header.equals(Arrays.asList("#League", "Team", "Number", "Name", "Position"))) {
                    throw new DataLoaderException("Header does not match expected: [#League,Team,Number,Name,Position]");
                }
                continue;
            }
            if (record.getRecordNumber() % 1000 == 0) {
                LOG.info("Loaded {} records", record.getRecordNumber());
            }

            League league = getLeague(record);
            Team team = getTeam(record, league);

            try {
                Player player = new Player(
                        record.get(3),
                        Byte.parseByte(record.get(2)),
                        Player.Position.valueOf(record.get(4)),
                        team.getId());
                playerDao.createPlayer(player);
            } catch (RuntimeException e) {
                LOG.warn("Skipped line [{}] because data is not valid: [{}]", record.getRecordNumber(), record.toString());
            }
        }
    }

    private Team getTeam(CSVRecord record, League league) {
        String teamName = record.get(1);
        Team team;
        if (teamCache.containsKey(teamName)) {
            team = teamCache.get(teamName);
        } else {
            if ((team = teamDao.getTeamByName(teamName)) == null) {
                team = teamDao.createTeam(new Team(teamName, league.getId()));
            }
            teamCache.put(teamName, team);
        }
        return team;
    }

    private League getLeague(CSVRecord record) {
        String leagueName = record.get(0);
        League league;
        if (leagueCache.containsKey(leagueName)) {
            league = leagueCache.get(leagueName);
        } else {
            if ((league = leagueDao.getLeagueByName(leagueName)) == null) {
                league = leagueDao.createLeague(new League(leagueName));
            }
            leagueCache.put(leagueName, league);
        }
        return league;
    }
}
