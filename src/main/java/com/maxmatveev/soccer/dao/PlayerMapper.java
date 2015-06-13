package com.maxmatveev.soccer.dao;

import com.google.common.collect.ImmutableMap;
import com.maxmatveev.soccer.model.Player;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Max Matveev on 09/06/15.
 */
@Component
public class PlayerMapper implements RowMapper<Player> {

    public static final String ID = "id";
    public static final String NUMBER = "number";
    public static final String TEAM_ID = "team_id";
    public static final String POSITION = "position";
    public static final String FULL_NAME = "full_name";

    @Override
    public Player mapRow(ResultSet rs, int rowNum) throws SQLException {
        Player player = new Player();
        player.setFullName(rs.getString(FULL_NAME));
        player.setId(rs.getLong(ID));
        player.setNumber(rs.getByte(NUMBER));
        player.setTeamId(rs.getLong(TEAM_ID));
        player.setPosition(Player.Position.valueOf(rs.getString(POSITION)));
        return player;
    }

    public Map<String, Object> mapPlayer(final Player player) {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
        builder.put(FULL_NAME, player.getFullName());
        builder.put(ID, player.getId());
        builder.put(NUMBER, player.getNumber());
        builder.put(TEAM_ID, player.getTeamId());
        builder.put(POSITION, player.getPosition().name());
        return builder.build();
    }
}
