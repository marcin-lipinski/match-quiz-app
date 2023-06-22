package pl.marcinlipinski.matchquizapp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class Event {
    Long id;
    Long homeTeamId;
    String homeTeam;
    String homeTeamLogo;
    Long awayTeamId;
    String awayTeam;
    String awayTeamLogo;
    LocalDate startTime;
    int homeTeamScore;
    int awayTeamScore;
    int winnerCode;
}
