package pl.marcinlipinski.matchquizapp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@SuperBuilder
public class Event {
    private Long id;
    private String homeTeam;
    private String homeTeamLogo;
    private String awayTeam;
    private String awayTeamLogo;
    private LocalDate startTime;
    private int homeTeamScore;
    private int awayTeamScore;
}
