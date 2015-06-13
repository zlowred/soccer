package com.maxmatveev.soccer.model;

/**
 * Created by Max Matveev on 09/06/15.
 */
public class Team {
    private long id;
    private String name;
    private long leagueId;

    public Team() {
    }

    public Team(String name, long leagueId) {
        this.name = name;
        this.leagueId = leagueId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(long leagueId) {
        this.leagueId = leagueId;
    }
}
