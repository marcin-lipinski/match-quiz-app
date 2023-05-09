package pl.marcinlipinski.matchquizapp;

import java.time.LocalDateTime;

public class Event {
    private Long id;
    private String status;
    private String homeTeam;
    private String homeTeamLogo;
    private String awayTeam;
    private String awayTeamLogo;
    private LocalDateTime startTime;
    private int homeTeamScore;
    private int awayTeamScore;
    private int winnerCode;
    private double homeTeamOdd;
    private double drawTeamOdd;
    private Double awayTeamOdd;
    private String leagueName;
    private String leagueLogo;
}
