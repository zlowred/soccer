package com.maxmatveev.soccer.dao;

import com.google.common.collect.ImmutableMap;
import com.google.gwt.thirdparty.guava.common.collect.Maps;
import com.maxmatveev.soccer.model.League;
import com.maxmatveev.soccer.model.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by Max Matveev on 09/06/15.
 */
@Repository
public class TeamDao {
    public static final String SELECT_TEAM_BY_NAME_SQL = "select * from Teams where name = :name";
    public static final String SELECT_TEAM_BY_ID_SQL = "select * from Teams where id = :id";
    public static final String CREATE_TEAM_SQL = "insert into Teams (name, league_id) values (:name, :league_id)";
    public static final String NAME = "name";
    public static final String ALL_TEAMS_SQL = "select * from Teams order by name";
    public static final String UPDATE_TEAM_SQL = "update Teams set name = :name, league_id = :league_id where id = :id";
    public static final String ID = "id";
    public static final String LEAGUE_IDS = "league_ids";
    public static final String SELECT_BY_LEAGUE_IDS_SQL = "select * from Teams where league_id in (:league_ids) order by name";
    private static final Logger LOG = LoggerFactory.getLogger(TeamDao.class);
    @Autowired
    private NamedParameterJdbcOperations jdbc;
    @Autowired
    private TeamMapper mapper;

    @Transactional(readOnly = true)
    public Team getTeamByName(String teamName) {
        try {
            return jdbc.queryForObject(SELECT_TEAM_BY_NAME_SQL, ImmutableMap.of(NAME, teamName), mapper);
        } catch (EmptyResultDataAccessException e) {
            LOG.trace("Team with name = [{}] wasn't found", teamName);
            return null;
        }
    }

    @Transactional(readOnly = true)
    public Collection<Team> getTeamsInLeagues(League... leagues) {
        List<Long> leagueIds = new ArrayList<>();
        Arrays.asList(leagues).stream().forEach(league -> leagueIds.add(league.getId()));
        return jdbc.query(SELECT_BY_LEAGUE_IDS_SQL, ImmutableMap.of(LEAGUE_IDS, leagueIds), mapper);
    }

    @Transactional(readOnly = true)
    public Team getTeamById(long teamId) {
        try {
            return jdbc.queryForObject(SELECT_TEAM_BY_ID_SQL, ImmutableMap.of(ID, teamId), mapper);
        } catch (EmptyResultDataAccessException e) {
            LOG.trace("League with id = [{}] wasn't found", teamId);
            return null;
        }
    }

    @Transactional
    public Team createTeam(Team team) {
        jdbc.update(CREATE_TEAM_SQL, mapper.mapTeam(team));
        return getTeamById(jdbc.queryForObject("call IDENTITY()", Maps.<String, Object>newHashMap(), Long.class));
    }

    @Transactional
    public boolean updateTeam(Team team) {
        return jdbc.update(UPDATE_TEAM_SQL, mapper.mapTeam(team)) == 1;
    }

    @Transactional(readOnly = true)
    public Collection<Team> getAllTeams() {
        return jdbc.query(ALL_TEAMS_SQL, mapper);
    }
}
