package com.maxmatveev.soccer.model;

/**
 * Created by Max Matveev on 09/06/15.
 */
public class Player {
    private String fullName;
    private byte number;
    private long id;
    private Position position;
    private long teamId;

    public Player(String fullName, byte number, Position position, long teamId) {
        this.fullName = fullName;
        this.number = number;
        this.position = position;
        this.teamId = teamId;
    }

    public Player() {

    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public byte getNumber() {
        return number;
    }

    public void setNumber(byte number) {
        this.number = number;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public static enum Position {
        G,
        D,
        M,
        F
    }
}
