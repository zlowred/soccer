package com.maxmatveev.soccer.dao;

import com.google.common.collect.ImmutableMap;
import com.google.gwt.thirdparty.guava.common.collect.Maps;
import com.maxmatveev.soccer.model.League;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * Created by Max Matveev on 09/06/15.
 */
@Repository
public class LeagueDao {
    public static final String SELECT_LEAGUE_BY_NAME_SQL = "select * from Leagues where name = :name";
    public static final String SELECT_LEAGUE_BY_ID_SQL = "select * from Leagues where id = :id";
    public static final String CREATE_LEAGUE_SQL = "insert into Leagues (name) values (:name)";
    public static final String NAME = "name";
    public static final String ALL_LEAGUES_SQL = "select * from Leagues order by name";
    public static final String UPDATE_LEAGUE_SQL = "update Leagues set name = :name where id = :id";
    public static final String ID = "id";
    private static final Logger LOG = LoggerFactory.getLogger(LeagueDao.class);
    @Autowired
    private NamedParameterJdbcOperations jdbc;
    @Autowired
    private LeagueMapper mapper;

    @Transactional(readOnly = true)
    public League getLeagueByName(String leagueName) {
        try {
            return jdbc.queryForObject(SELECT_LEAGUE_BY_NAME_SQL, ImmutableMap.of(NAME, leagueName), mapper);
        } catch (EmptyResultDataAccessException e) {
            LOG.trace("League with name = [{}] wasn't found", leagueName);
            return null;
        }
    }

    @Transactional(readOnly = true)
    public League getLeagueById(long leagueId) {
        try {
            return jdbc.queryForObject(SELECT_LEAGUE_BY_ID_SQL, ImmutableMap.of(ID, leagueId), mapper);
        } catch (EmptyResultDataAccessException e) {
            LOG.trace("League with id = [{}] wasn't found", leagueId);
            return null;
        }
    }

    @Transactional
    public League createLeague(League league) {
        jdbc.update(CREATE_LEAGUE_SQL, mapper.mapLeague(league));
        return getLeagueById(jdbc.queryForObject("call IDENTITY()", Maps.<String, Object>newHashMap(), Long.class));
    }

    @Transactional
    public boolean updateLeague(League league) {
        return jdbc.update(UPDATE_LEAGUE_SQL, mapper.mapLeague(league)) == 1;
    }

    @Transactional(readOnly = true)
    public Collection<League> getAllLeagues() {
        return jdbc.query(ALL_LEAGUES_SQL, mapper);
    }
}
