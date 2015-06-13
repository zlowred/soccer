package com.maxmatveev.soccer.ui;

import com.maxmatveev.soccer.model.League;
import com.maxmatveev.soccer.model.Team;
import com.maxmatveev.soccer.ui.model.DatabaseRecord;
import com.maxmatveev.soccer.ui.service.DatabaseService;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
@SpringUI
public class DatabaseUi extends UI {
    @Autowired
    private DatabaseService databaseService;
    private ComboBox leagues;
    private ComboBox teams;
    private League anyLeague;
    private BeanItemContainer<League> leagueContainer;
    private Team anyTeam;
    private BeanItemContainer<Team> teamContainer;
    private BeanItemContainer<DatabaseRecord> players;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setMargin(true);
        setContent(layout);

        FormLayout filters = new FormLayout();

        leagueContainer = new BeanItemContainer<>(League.class);
        leagues = new ComboBox("Select League", leagueContainer);
        leagues.setNullSelectionAllowed(false);
        leagues.setItemCaptionPropertyId("name");
        leagues.setWidth("300px");
        leagues.setFilteringMode(FilteringMode.CONTAINS);

        anyLeague = new League("All");
        leagueContainer.addItem(anyLeague);
        leagues.select(anyLeague);
        leagueContainer.addAll(databaseService.getAllLeagues());

        filters.addComponent(leagues);

        teamContainer = new BeanItemContainer<>(Team.class);
        teams = new ComboBox("Select Team", teamContainer);
        teams.setNullSelectionAllowed(false);
        teams.setItemCaptionPropertyId("name");
        teams.setWidth("300px");
        teams.setFilteringMode(FilteringMode.CONTAINS);

        anyTeam = new Team("All", 0);
        teamContainer.addItem(anyTeam);
        teams.select(anyTeam);
        teamContainer.addAll(databaseService.getAllTeams());


        filters.addComponent(teams);

        filters.setSizeUndefined();
        layout.addComponent(filters);

        players = new BeanItemContainer<>(DatabaseRecord.class);

        Table table = new Table("Players", players);
        table.setSizeFull();
        layout.addComponent(table);
        table.setVisibleColumns("league", "team", "number", "fullName", "position");
        table.setColumnHeaders("League", "Team", "Number", "Full Name", "Position");
        table.setSortEnabled(true);
        table.setSortContainerPropertyId("league");

        layout.setExpandRatio(table, 1);

        players.addAll(databaseService.getAllRecords());
        teams.addValueChangeListener(event -> teamChanged());
        leagues.addValueChangeListener(event -> leagueChanged());
    }

    private void teamChanged() {
        players.removeAllItems();
        if (anyTeam.equals(teams.getValue())) {
            if (anyLeague.equals(leagues.getValue())) {
                players.addAll(databaseService.getAllRecords());
            } else {
                players.addAll(databaseService.getRecordsForLeague((League) leagues.getValue()));
            }
        } else {
            players.addAll(databaseService.getRecordsForTeam((Team) teams.getValue()));
        }
    }

    private void leagueChanged() {
        teamContainer.removeAllItems();
        teamContainer.addItem(anyTeam);
        teams.select(anyTeam);
        players.removeAllItems();
        if (anyLeague.equals(leagues.getValue())) {
            teamContainer.addAll(databaseService.getAllTeams());
            players.addAll(databaseService.getAllRecords());
        } else {
            teamContainer.addAll(databaseService.getTeamsInLeague((League) leagues.getValue()));
            players.addAll(databaseService.getRecordsForLeague((League) leagues.getValue()));
        }
    }
}
