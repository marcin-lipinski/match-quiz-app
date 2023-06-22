package pl.marcinlipinski.matchquizapp.models.DTOs;

import com.google.gson.annotations.SerializedName;
import pl.marcinlipinski.matchquizapp.models.Event;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EventDAO {
    public Long id;
    public String status;
    @SerializedName("home_team")
    public Team homeTeam;
    @SerializedName("away_team")
    public Team awayTeam;
    @SerializedName("start_at")
    public String startTime;
    @SerializedName("home_score")
    public Score homeScore;
    @SerializedName("away_score")
    public Score awayScore;
    @SerializedName("winner_code")
    public int winnerCode;

    public Event getEvent() {
        return Event.builder()
                .id(this.id)
                .homeTeamId(this.homeTeam.id).homeTeam(this.homeTeam.name).homeTeamLogo(this.homeTeam.logo)
                .awayTeamId(this.awayTeam.id).awayTeam(this.awayTeam.name).awayTeamLogo(this.awayTeam.logo)
                .startTime(LocalDate.parse(this.startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .homeTeamScore(this.homeScore.current)
                .awayTeamScore(this.awayScore.current)
                .winnerCode(this.winnerCode).build();
    }
}

class Team {
    public Long id;
    @SerializedName("name_full")
    public String name;
    public String logo;
}

class Score {
    @SerializedName("normal_time")
    public int current;
}

