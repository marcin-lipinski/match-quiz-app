package pl.marcinlipinski.matchquizapp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@SuperBuilder
public class Event {
    private Long id;
    private Long homeTeamId;
    private String homeTeam;
    private String homeTeamLogo;
    private Long awayTeamId;
    private String awayTeam;
    private String awayTeamLogo;
    private LocalDate startTime;
    private int homeTeamScore;
    private int awayTeamScore;
    private int winnerCode;
}
