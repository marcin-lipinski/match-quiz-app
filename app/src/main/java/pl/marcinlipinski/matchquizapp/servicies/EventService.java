package pl.marcinlipinski.matchquizapp.servicies;

import android.content.ContentValues;
import android.content.Context;
import org.jetbrains.annotations.NotNull;
import pl.marcinlipinski.matchquizapp.R;
import pl.marcinlipinski.matchquizapp.database.DatabaseContext;
import pl.marcinlipinski.matchquizapp.models.DTOs.EventDAO;
import pl.marcinlipinski.matchquizapp.models.DTOs.TeamDTO;
import pl.marcinlipinski.matchquizapp.models.Event;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.ArrayList;

public class EventService implements Service<Event> {

    DatabaseContext databaseContext;

    ApiService apiService;

    @Inject
    public EventService(DatabaseContext databaseContext, ApiService apiService) {
        this.databaseContext = databaseContext;
        this.apiService = apiService;
        this.initialize();
    }

    public void initialize() {
        databaseContext.query("CREATE TABLE if not exists EVENT_TABLE (ID INTEGER PRIMARY KEY, " +
                "HOME_TEAM TEXT, AWAY_TEAM TEXT, HOME_TEAM_LOGO TEXT, AWAY_TEAM_LOGO TEXT, " +
                "HOME_TEAM_SCORE TEXT, AWAY_TEAM_SCORE TEXT, START_TIME TEXT)");
    }

    @Override
    public ContentValues newContent(Event event) {
        ContentValues cv = new ContentValues();
        cv.put("ID", event.getId());
        cv.put("HOME_TEAM", event.getHomeTeam());
        cv.put("AWAY_TEAM", event.getAwayTeam());
        cv.put("HOME_TEAM_LOGO", event.getHomeTeamLogo());
        cv.put("AWAY_TEAM_LOGO", event.getAwayTeamLogo());
        cv.put("HOME_TEAM_SCORE", event.getAwayTeamScore());
        cv.put("AWAY_TEAM_SCORE", event.getAwayTeamScore());
        cv.put("START_TIME", event.getStartTime().toString());
        return cv;
    }

    public void getEventsBySeasonId(Long seasonId, Context context, RetrofitCallback<ArrayList<Event>> volleyCallback) {
        String apiKey = context.getResources().getString(R.string.api_key);
        String apiHost = context.getResources().getString(R.string.api_host_key);

        apiService.getEventsBySeasonId(seasonId, apiKey, apiHost).enqueue(new Callback<ApiResponse<ArrayList<EventDAO>>>() {
            @Override
            public void onResponse(@NotNull Call<ApiResponse<ArrayList<EventDAO>>> call, @NotNull Response<ApiResponse<ArrayList<EventDAO>>> response) {
                if (!response.isSuccessful()) volleyCallback.onFail(response.message());
                try {
                    assert response.body() != null;
                    ArrayList<Event> events = new ArrayList<>();
                    for (EventDAO ev : response.body().data) {
                        if (ev.status.equals("finished")) events.add(ev.getEvent());
                    }
                    volleyCallback.onSuccess(events);
                } catch (Exception e) {
                    volleyCallback.onFail(e.getMessage());
                }
            }

            @Override
            public void onFailure(@NotNull Call<ApiResponse<ArrayList<EventDAO>>> call, @NotNull Throwable throwable) {
                volleyCallback.onFail(throwable.getMessage());
            }
        });
    }

    public void getCityDistanceByWinnerTeamId(Long teamId, Context context, RetrofitCallback<String> volleyCallback) {
        String apiKey = context.getResources().getString(R.string.api_key);
        String apiHost = context.getResources().getString(R.string.api_host_key);

        apiService.getCityOfTeam(teamId, apiKey, apiHost).enqueue(new Callback<ApiResponse<TeamDTO>>() {
            @Override
            public void onResponse(@NotNull Call<ApiResponse<TeamDTO>> call, @NotNull Response<ApiResponse<TeamDTO>> response) {
                if (!response.isSuccessful()) volleyCallback.onFail(response.message());
                try {
                    volleyCallback.onSuccess(response.body().data.getCity());
                } catch (Exception e) {
                    volleyCallback.onFail(e.getMessage());
                }
            }

            @Override
            public void onFailure(@NotNull Call<ApiResponse<TeamDTO>> call, @NotNull Throwable throwable) {
                volleyCallback.onFail(throwable.getMessage());
            }
        });
    }
}
