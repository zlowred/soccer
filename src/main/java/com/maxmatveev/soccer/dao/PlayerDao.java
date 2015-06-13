package com.maxmatveev.soccer.dao;

import com.google.common.collect.ImmutableMap;
import com.google.gwt.thirdparty.guava.common.collect.Maps;
import com.maxmatveev.soccer.model.League;
import com.maxmatveev.soccer.model.Player;
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
public class PlayerDao {
    public static final String SELECT_BY_LEAGUE_IDS_SQL = "select * from Players where team_id in (select id from Teams where league_id in (:league_ids)) order by full_name";
    public static final String SELECT_BY_TEAM_IDS_SQL = "select * from Players where team_id in (:team_ids) order by full_name";
    public static final String LEAGUE_IDS = "league_ids";
    public static final String SELECT_PLAYER_SQL = "select * from Players where id = :id";
    public static final String CREATE_PLAYES_SQL = "insert into Players(full_name, number, position, team_id) values (:full_name, :number, :position, :team_id)";
    public static final String ALL_PLAYERS_SQL = "select * from Players order by full_name";
    public static final String UPDATE_PLAYES_SQL = "update Players set full_name = :full_name, number = :number, position = :position, team_id = :team_id where id = :id";
    public static final String TEAM_IDS = "team_ids";
    private static final Logger LOG = LoggerFactory.getLogger(PlayerDao.class);

    @Autowired
    private NamedParameterJdbcOperations jdbc;
    @Autowired
    private PlayerMapper mapper;

    @Transactional(readOnly = true)
    public Player getPlayerById(long playerId) {
        try {
            return jdbc.queryForObject(SELECT_PLAYER_SQL, ImmutableMap.of("id", playerId), mapper);
        } catch (EmptyResultDataAccessException e) {
            LOG.trace("Player with id = [{}] wasn't found", playerId);
            return null;
        }
    }

    @Transactional(readOnly = true)
    public Collection<Player> getPlayersInLeagues(League... leagues) {
        List<Long> leagueIds = new ArrayList<>();
        Arrays.asList(leagues).stream().forEach(league -> leagueIds.add(league.getId()));
        return jdbc.query(SELECT_BY_LEAGUE_IDS_SQL, ImmutableMap.of(LEAGUE_IDS, leagueIds), mapper);
    }

    @Transactional(readOnly = true)
    public Collection<Player> getPlayersInTeams(Team... teams) {
        List<Long> teamIds = new ArrayList<>();
        Arrays.asList(teams).stream().forEach(team -> teamIds.add(team.getId()));
        return jdbc.query(SELECT_BY_TEAM_IDS_SQL, ImmutableMap.of(TEAM_IDS, teamIds), mapper);
    }


    @Transactional
    public Player createPlayer(Player player) {
        jdbc.update(CREATE_PLAYES_SQL, mapper.mapPlayer(player));
        return getPlayerById(jdbc.queryForObject("call IDENTITY()", Maps.<String, Object>newHashMap(), Long.class));
    }

    @Transactional
    public boolean updatePlayer(Player player) {
        return jdbc.update(UPDATE_PLAYES_SQL, mapper.mapPlayer(player)) == 1;
    }

    @Transactional(readOnly = true)
    public Collection<Player> getAllPlayers() {
        return jdbc.query(ALL_PLAYERS_SQL, mapper);
    }
}
