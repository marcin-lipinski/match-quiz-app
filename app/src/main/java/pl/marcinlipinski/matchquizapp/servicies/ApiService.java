package pl.marcinlipinski.matchquizapp.servicies;

import pl.marcinlipinski.matchquizapp.models.DTOs.EventDAO;
import pl.marcinlipinski.matchquizapp.models.DTOs.TeamDTO;
import pl.marcinlipinski.matchquizapp.models.Season;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import javax.inject.Singleton;
import java.util.ArrayList;

@Singleton
public interface ApiService {
    @GET("leagues/{leagueId}/seasons")
    Call<ApiResponse<ArrayList<Season>>> getSeasonsByLeagueId(
            @Path("leagueId") long leagueId,
            @Header("X-RapidAPI-Key") String apiKey,
            @Header("X-RapidAPI-Host") String apiHost);

    @GET("/seasons/{seasonId}/events")
    Call<ApiResponse<ArrayList<EventDAO>>> getEventsBySeasonId(
            @Path("seasonId") long seasonId,
            @Header("X-RapidAPI-Key") String apiKey,
            @Header("X-RapidAPI-Host") String apiHost);

    @GET("/teams/{teamId}")
    Call<ApiResponse<TeamDTO>> getCityOfTeam(
            @Path("teamId") long teamId,
            @Header("X-RapidAPI-Key") String apiKey,
            @Header("X-RapidAPI-Host") String apiHost);
}
