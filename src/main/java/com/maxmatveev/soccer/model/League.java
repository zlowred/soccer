package com.maxmatveev.soccer.model;

/**
 * Created by Max Matveev on 09/06/15.
 */
public class League {
    private long id;
    private String name;

    public League() {
    }

    public League(String name) {
        this.name = name;
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
}
